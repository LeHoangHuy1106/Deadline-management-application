package com.example.excercise_lab10;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class customAdapter extends RecyclerView.Adapter<customAdapter.ViewHolder>{

    private DanhSachDeadline danhsach;
    private Context context;

    public customAdapter(Context context, DanhSachDeadline danhsach) {
        this.context = context;
        this.danhsach = danhsach;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deadline_row,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }
    public void release(){
        context=null;
    }
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull customAdapter.ViewHolder holder, int position) {
        deadline p= danhsach.get(position);
        if(p==null)
            return;
        holder.monhoc.setText(p.getMonHoc());
        holder.ngaygiao.setText(p.getNgayGiao());
        holder.hannop.setText(p.getHanNop());
        holder.noidung.setText(p.getNoiDung());
        holder.stt.setText(p.getStt()+"");
        holder.trangthai.setText(p.getTrangThai());


    }


    @Override
    public int getItemCount() {
        if( danhsach!=null)
            return danhsach.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView monhoc,ngaygiao,hannop,noidung,stt,trangthai;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            monhoc= itemView.findViewById(R.id.txtMonHoc);
            ngaygiao= itemView.findViewById(R.id.txtNgayGiao);
            hannop = itemView.findViewById(R.id.txtNgayNop);
            noidung= itemView.findViewById(R.id.txtNoiDung);
            stt= itemView.findViewById(R.id.txtSTT);
            trangthai= itemView.findViewById(R.id.txtTrangThai);

            noidung.setOnCreateContextMenuListener(this);
            trangthai.setOnCreateContextMenuListener(this);
            stt.setOnCreateContextMenuListener(this);
            hannop.setOnCreateContextMenuListener(this);
            ngaygiao.setOnCreateContextMenuListener(this);
            monhoc.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("LỰA CHỌN CỦA BẠN");
            menu.add(this.getAdapterPosition(),123,0,"Hoàn Thành");
            menu.add(this.getAdapterPosition(),456,1,"Xóa");
            menu.add(this.getAdapterPosition(),789,2,"Sửa");


        }


    }
    public void  removeItem(int positon){
        danhsach.removeAt(positon);
        notifyDataSetChanged();
    }

}
