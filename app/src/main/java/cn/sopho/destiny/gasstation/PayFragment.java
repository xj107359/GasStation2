package cn.sopho.destiny.gasstation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.sopho.destiny.camviewlibrary.ScannerLiveView;
import cn.sopho.destiny.camviewlibrary.camera.CameraController;
import cn.sopho.destiny.camviewlibrary.scanner.decoder.zxing.ZXDecoder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {
    //    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private ScannerLiveView camera;
    private CameraController controller;

    private OnFragmentInteractionListener mListener;

    public PayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static PayFragment newInstance() {//String param1, String param2
        PayFragment fragment = new PayFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pay, container, false);

        camera = (ScannerLiveView) rootView.findViewById(R.id.camview);

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
//                Toast.makeText(getActivity(), "开始扫描", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
//                Toast.makeText(getActivity(), "停止扫描", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                Toast.makeText(getActivity(), "扫描错误: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
//        return inflater.inflate(R.layout.fragment_pay, container, false);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            //相当于Fragment的onResume
//            if (camera != null) {
//                ZXDecoder decoder = new ZXDecoder();
//                decoder.setScanAreaPercent(0.5);
//                camera.setDecoder(decoder);
//                camera.startScanner();
//            }
//        } else {
//            //相当于Fragment的onPause
//            if (camera != null)
//                camera.stopScanner();
//        }
//    }

    @Override
    public void onResume() {
        if (camera != null) {
            ZXDecoder decoder = new ZXDecoder();
            decoder.setScanAreaPercent(0.5);
            camera.setDecoder(decoder);
            camera.startScanner();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (camera != null)
            camera.stopScanner();
        super.onPause();
    }

    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
