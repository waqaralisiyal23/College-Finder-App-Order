package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithwaqar.myapplication.AdminUpdateDataActivity;
import com.codewithwaqar.myapplication.LoginActivity;
import com.codewithwaqar.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import models.Institute;

public class InstituteAdapter extends FirestoreRecyclerAdapter<Institute, InstituteAdapter.InstituteHolder> {

    private Context context;
    private boolean isEditable = false;

    public InstituteAdapter(Context context, @NonNull FirestoreRecyclerOptions<Institute> options, boolean isEditable) {
        super(options);
        this.context = context;
        this.isEditable = isEditable;
    }

    @Override
    protected void onBindViewHolder(@NonNull InstituteHolder holder, int position, @NonNull final Institute institute) {
        Picasso.get().load(institute.getImageUrl()).placeholder(R.drawable.placeholder_image)
                .into(holder.imageView);
        holder.name.setText(institute.getName());
        holder.address.setText(institute.getAddress());

        if(isEditable){
            holder.type.setVisibility(View.VISIBLE);
            holder.buttonsLayout.setVisibility(View.VISIBLE);

            holder.type.setText("Type: "+institute.getType());

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
    }

    @NonNull
    @Override
    public InstituteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.institute_row, parent, false);
        return new InstituteHolder(view);
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

    class InstituteHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        TextView address;
        TextView type;
        LinearLayout buttonsLayout;
        Button editButton;
        Button deleteButton;

        public InstituteHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.intitute_row_image_view);
            name = (TextView) itemView.findViewById(R.id.intitute_row_name);
            address = (TextView) itemView.findViewById(R.id.intitute_row_address);
            type = (TextView) itemView.findViewById(R.id.intitute_row_type);
            buttonsLayout = (LinearLayout) itemView.findViewById(R.id.intitute_row_buttons_layout);
            editButton = (Button) itemView.findViewById(R.id.intitute_row_edit_button);
            deleteButton = (Button) itemView.findViewById(R.id.intitute_row_delete_button);
        }
    }
}
