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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.navBar;

import java.io.IOException;


public class SeePictureFragment extends Fragment {
    private SharedViewModel viewModel;

    private Button deletePicture, updatePicture;
    private ImageButton backButton;

    private RequestQueue mQueue;

    private String URL = "http://coms-309-032.class.las.iastate.edu:8080";




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
                    }
                }
            });




}