package PictureData;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.navBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import UserInfo.UserSession;


public class SeePictureFragment extends Fragment {
    private SharedViewModel viewModel;

    private Button deletePicture, updatePicture;
    private ImageButton backButton;

    private RequestQueue mQueue;

    private String URL = UserSession.getInstance().getURL();




    public SeePictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeePictureFragment newInstance(String param1, String param2) {
        SeePictureFragment fragment = new SeePictureFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get mQueue from navbar
        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            mQueue = navBar.getQueue();
        }
        else {
            Log.e("Picture", "Error getting mQueue from navbar");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    private void sendDataBack(Bitmap selectedImageBitmap) {
        viewModel.setData(selectedImageBitmap);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile_picture_popup, container, false);

        deletePicture = view.findViewById(R.id.deleteProfilePicture);
        updatePicture = view.findViewById(R.id.updateProfilePicture);
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go back to the conversation fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(SeePictureFragment.this);
                transaction.commit();

                getActivity().findViewById(R.id.popout_frame_layout).setVisibility(View.GONE);

            }
        });

        deletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataBack(null);
                deleteImage();
            }


        });

        updatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();


            }
        });
        return view;
    }

    void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        gallery.launch(intent);

    }

    ActivityResultLauncher<Intent> gallery
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContext().getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
//                        profilePicture.setImageBitmap(
//                                selectedImageBitmap);
                        sendDataBack(selectedImageBitmap);
                        uploadImage(selectedImageBitmap);


                    }
                }
            });

    private void deleteImage() {
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.DELETE,
                URL + "/users/icons",
                null,
                response -> {
                    // Handle response
                    Log.d("Delete", "Response: " + response);
                },
                error -> {
                    // Handle error
                    Log.e("Delete", "Error: " + error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        mQueue.add(multipartRequest);
    }


    private void uploadImage(Bitmap selectedImageBitMap){

        // Is it required to use bitmaps?
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImageBitMap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] imageData = stream.toByteArray();
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                URL + "/users/icons",
                imageData,
                response -> {
                    // Handle response
                    Log.d("Upload", "Response: " + response);
                },
                error -> {
                    if (error == null || error.networkResponse == null) {
                        return;
                    }
                    String body = "";
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // exception
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };

        mQueue.add(multipartRequest);
    }





}