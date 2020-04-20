package com.app.civitati.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;
import com.app.civitati.ui.home.uploadImage;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UpdateNeedyFragment extends Fragment implements View.OnClickListener {
    View root;
    TextView needyName;
    TextView helpReason;
    TextView needyAddress;
    TextView needyTelephone;
    ImageView userAvatar;
    String userAvatarFileName;

    public Needy needies;
    Context context;

    UpdateNeedyFragment(Needy needies, Context context) {
        this.needies = needies;
        this.context = context;
        userAvatarFileName = "";

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_upd_needy, container, false);

        needyName = (TextView)root.findViewById(R.id.needyName);
        helpReason = (TextView)root.findViewById(R.id.helpReason);
        needyAddress = (TextView)root.findViewById(R.id.needyAddress);
        needyTelephone = (TextView)root.findViewById(R.id.needyTelephone);
        userAvatar = (ImageView) root.findViewById(R.id.userAvatar);

        needyName.setText(needies.getName());
        helpReason.setText(needies.getHelpReason());
        needyAddress.setText(needies.getAdress());
        needyTelephone.setText(needies.getTelephone().toString());

        Button btn = root.findViewById(R.id.needySubmitBtn);
        btn.setOnClickListener(this);

        Button btnPhoto = root.findViewById(R.id.uploadImageBtn);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    startGallery();
                }
            }
            }
        );

        return root;
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                String picturePath = getPath( getActivity( ).getApplicationContext( ), returnUri );
                File file = new File(picturePath);
                userAvatarFileName = file.getName();
                Log.d("Picture Path", picturePath);
                Uri returnUri2 = Uri.fromFile(new File(picturePath));
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), returnUri2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                userAvatar.setImageBitmap(bitmapImage);

                uploadImage uploadImages = new uploadImage(context, picturePath);
                uploadImages.execute();

            }
        }
        //Uri returnUri;
        //returnUri = data.getData();
    }

    public static String getPath( Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> tryToDelete = apiInterface.updNeedy(needies.getId(), needyName.getText().toString(), helpReason.getText().toString(), needyAddress.getText().toString(),needyTelephone.getText().toString(), userAvatarFileName, "UR" );
        tryToDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String success = "Record was updated successfully";
                String reponseString = null;
                try {
                    reponseString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(reponseString);
                Log.i("Civitati",  reponseString );
                if(success.equals(reponseString)) {
                    Log.i("Civitati", "Success to update row. ");
                    Toast.makeText(context,getContext().getString(R.string.report_change_success), Toast.LENGTH_SHORT).show();

                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                    Button btn = root.findViewById(R.id.needySubmitBtn);
                    btn.setVisibility(View.INVISIBLE);
                    Button btn2 = root.findViewById(R.id.uploadImageBtn);
                    btn2.setVisibility(View.INVISIBLE);
                    needyName.setVisibility(View.INVISIBLE);
                    helpReason.setVisibility(View.INVISIBLE);
                    needyAddress.setVisibility(View.INVISIBLE);
                    needyTelephone.setVisibility(View.INVISIBLE);
                    userAvatar.setVisibility(View.INVISIBLE);

                    transaction.replace(R.id.NeedyUpd,  new DashboardFragment());
                    transaction.commit();
                }
                else {
                    Toast.makeText(context,getContext().getString(R.string.report_update_fail), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Civitati", "Error: Record was not updated" );
            }
        });

    }
}