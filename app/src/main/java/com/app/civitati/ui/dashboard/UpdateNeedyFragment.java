package com.app.civitati.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public Needy needies;
    Context context;

    UpdateNeedyFragment(Needy needies, Context context) {
        this.needies = needies;
        this.context = context;

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

        Button btn = root.findViewById(R.id.needyHelpBtn);
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
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userAvatar.setImageBitmap(bitmapImage);
            }
        }
        //Uri returnUri;
        //returnUri = data.getData();
    }

    @Override
    public void onClick(View v) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        Call<ResponseBody> tryToDelete = apiInterface.updNeedy(needies.getId(), needyName.getText().toString(), helpReason.getText().toString(), needyAddress.getText().toString(),needyTelephone.getText().toString(), "UR" );
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
                    Toast.makeText(context,getContext().getString(R.string.report_update_success), Toast.LENGTH_SHORT).show();

                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                    Button btn = root.findViewById(R.id.needyHelpBtn);
                    btn.setVisibility(View.INVISIBLE);
                    needyName.setVisibility(View.INVISIBLE);
                    helpReason.setVisibility(View.INVISIBLE);
                    needyAddress.setVisibility(View.INVISIBLE);
                    needyTelephone.setVisibility(View.INVISIBLE);

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