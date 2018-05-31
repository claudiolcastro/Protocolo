package app.claudiomobiledev.protocolo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;


/**
 * Created by claudiolcastro on 26/03/17.
 */

public class Fragment_Assinatura extends Fragment {

    private SignaturePad mSignaturePad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ass_layout, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mSignaturePad = (SignaturePad) getActivity().findViewById(R.id.signature_pad);


        return view;
    }
}
