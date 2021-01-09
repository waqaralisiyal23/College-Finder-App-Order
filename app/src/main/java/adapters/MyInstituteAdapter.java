package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithwaqar.myapplication.AdminUpdateDataActivity;
import com.codewithwaqar.myapplication.R;
import com.codewithwaqar.myapplication.ViewInstituteDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import models.Institute;

public class MyInstituteAdapter extends RecyclerView.Adapter<MyInstituteAdapter.InstituteViewHolder> {

    private Context context;
    private List<Institute> instituteList;
    private boolean isEditable = false;

    public MyInstituteAdapter(Context context, List<Institute> instituteList, boolean isEditable) {
        this.context = context;
        this.instituteList = instituteList;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public InstituteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.institute_row, parent, false);
        return new InstituteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstituteViewHolder holder, int position) {
        final Institute institute = instituteList.get(position);
        Picasso.get().load(institute.getImageUrl()).placeholder(R.drawable.placeholder_image)
                .into(holder.imageView);
        holder.name.setText(institute.getName());
        holder.address.setText(institute.getAddress());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewInstituteDetailActivity.class);
                intent.putExtra("institute", institute);
                context.startActivity(intent);
            }
        });

        if(institute.getDepartmentList()!=null){
            holder.departmentsTV.setVisibility(View.VISIBLE);
            holder.departmentList.setVisibility(View.VISIBLE);
            for(int i=0; i<institute.getDepartmentList().size(); i++){
                holder.departmentList.setText(holder.departmentList.getText()+"- "+
                        institute.getDepartmentList().get(i)+"\n");
            }
        }

        if(isEditable){
            holder.type.setVisibility(View.VISIBLE);
            holder.buttonsLayout.setVisibility(View.VISIBLE);

            if(institute.getType().equals("university")){
                holder.type.setText("Type: University");
            }
            else{
                holder.type.setText("Type: College");
            }

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdminUpdateDataActivity.class);
                    intent.putExtra("id", institute.getId());
                    context.startActivity(intent);
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Institute")
                            .setMessage("Are you sure you want to delete this institute?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    deleteInstitute(institute.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
        else{
            holder.visitButton.setVisibility(View.VISIBLE);
            holder.visitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(institute.getUrl()));
                        context.startActivity(browserIntent);
                    }catch (Exception e){
                        Toast.makeText(context, "Could not launch url", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return instituteList.size();
    }

    public void filterList(List<Institute> filteredList){
        instituteList = filteredList;
        notifyDataSetChanged();
    }

    private void deleteInstitute(String id){
        FirebaseFirestore.getInstance().collection("institutes").document(id)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Deleted Successfully",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "Error: "+
                            task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    class InstituteViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView name;
        TextView address;
        TextView type;
        LinearLayout buttonsLayout;
        Button editButton;
        Button deleteButton;
        TextView departmentsTV;
        TextView departmentList;
        Button visitButton;

        public InstituteViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.intitute_row_cv);
            imageView = (ImageView) itemView.findViewById(R.id.intitute_row_image_view);
            name = (TextView) itemView.findViewById(R.id.intitute_row_name);
            address = (TextView) itemView.findViewById(R.id.intitute_row_address);
            type = (TextView) itemView.findViewById(R.id.intitute_row_type);
            buttonsLayout = (LinearLayout) itemView.findViewById(R.id.intitute_row_buttons_layout);
            editButton = (Button) itemView.findViewById(R.id.intitute_row_edit_button);
            deleteButton = (Button) itemView.findViewById(R.id.intitute_row_delete_button);
            departmentsTV = (TextView) itemView.findViewById(R.id.intitute_row_departments);
            departmentList = (TextView) itemView.findViewById(R.id.intitute_row_department_list);
            visitButton = (Button) itemView.findViewById(R.id.intitute_row_visit_button);
        }
    }
}
