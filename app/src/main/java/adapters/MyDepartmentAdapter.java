package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithwaqar.myapplication.R;

import java.util.List;

public class MyDepartmentAdapter extends RecyclerView.Adapter<MyDepartmentAdapter.DepartmentViewHolder> {

    private Context context;
    private List<String> departmentList;

    public MyDepartmentAdapter(Context context, List<String> departmentList){
        this.context = context;
        this.departmentList = departmentList;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_row, parent, false);
        return new MyDepartmentAdapter.DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, final int position) {
        final String name = departmentList.get(position);
        holder.departmentName.setText(name);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Department")
                        .setMessage("Are you sure you want to delete this department?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteDepartment(position);
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

    private void deleteDepartment(int position){
        departmentList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    public List<String> getDepartmentList(){
        return departmentList;
    }

    class DepartmentViewHolder extends RecyclerView.ViewHolder {

        TextView departmentName;
        Button deleteButton;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);

            departmentName = (TextView) itemView.findViewById(R.id.department_row_dname);
            deleteButton = (Button) itemView.findViewById(R.id.department_row_delete_button);
        }
    }

}
