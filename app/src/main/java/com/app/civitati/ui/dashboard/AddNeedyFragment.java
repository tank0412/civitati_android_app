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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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

public class AddNeedyFragment extends Fragment implements View.OnClickListener {
    View root;
    private String userAvatarFileName;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_needy, container, false);
        Button addNeedyBtn = root.findViewById(R.id.needySubmitBtn);
        addNeedyBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        final TextView needyName = root.findViewById(R.id.needyName);
        final TextView helpReason = root.findViewById(R.id.helpReason);
        final TextView needyAddress = root.findViewById(R.id.needyAddress);
        final TextView needyTelephone = root.findViewById(R.id.needyTelephone);
        final TextView needyAddInfo = root.findViewById(R.id.needyAddInfo);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        final APIInterface apiInterfaceF = apiInterface;
        Call<ResponseBody> getNeedyCount = apiInterface.needyCount("SC" );
        getNeedyCount.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String reponse = response.body().string();
                    int needyCount = Integer.parseInt(reponse);
                    if(userAvatarFileName != null) {
                        Log.i("Civitati", userAvatarFileName);
                    }
                    else {
                        userAvatarFileName= "sampleavatar.png";
                    }

                    Call<ResponseBody> getNeedyCount = apiInterfaceF.addNeedy(needyCount+1, needyName.getText().toString(), helpReason.getText().toString(), needyAddress.getText().toString(), needyTelephone.getText().toString(), userAvatarFileName, "I" );
                    getNeedyCount.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String success = "New record created successfully";
                                String reponse = response.body().string();
                                if(success.equals(reponse)) {
                                    System.out.println("Add needy success");
                                    Log.i("Civitati", "Success to add needy. ");
                                    needyAddInfo.setText(getContext().getString(R.string.report_add_success));
                                    needyAddInfo.setVisibility(View.VISIBLE);
                                }
                                else {
                                    System.out.println("Add needy fail");
                                    Log.i("Civitati", "Fail to add needy. ");
                                    needyAddInfo.setText(getContext().getString(R.string.report_add_fail));
                                    needyAddInfo.setVisibility(View.VISIBLE);

                                }
                                System.out.println(reponse);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //e.printStackTrace();
                            Log.i("Civitati", "FAIL to insert Needy. Error" );
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //e.printStackTrace();
                Log.i("Civitati", "FAIL to get count of Needy. Error" );
            }
        });
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
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
                ImageView userAvatar = root.findViewById(R.id.userAvatar);
                Log.d("Picture Path", picturePath);
                Uri returnUri2 = Uri.fromFile(new File(picturePath));
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), returnUri2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                userAvatar.setImageBitmap(bitmapImage);

                uploadImage uploadImages = new uploadImage(getContext(), picturePath);
                uploadImages.execute();

            }
        }
        //Uri returnUri;
        //returnUri = data.getData();
    }

    public static String getPath(Context context, Uri uri ) {
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
}
