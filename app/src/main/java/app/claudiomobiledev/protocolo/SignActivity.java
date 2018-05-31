package app.claudiomobiledev.protocolo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by claudiolcastro on 30/03/17.
 */

public class SignActivity extends Activity {

    private String gDocumento;
    private String gCliente;
    private String gData;
    private String gHora;
    private String gEntregador;
    private String gRecebedor;
    private String gObservacao;
    private String gTipo;

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private  Button mSendButton;

    private ProgressDialog progressDialog;

    private String imgSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);
                mSendButton.setEnabled(true);

            }

            @Override
            public void onClear() {
                mClearButton.setEnabled(false);
                mSendButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSendButton = (Button) findViewById(R.id.send_button);

        //Clear Sign Draw
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        //Send Protocol with Signed Draw
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gDocumento = getIntent().getStringExtra("pDocumento");
                gCliente = getIntent().getStringExtra("pCliente");
                gData = getIntent().getStringExtra("pData");
                gHora = getIntent().getStringExtra("pHora");
                gEntregador = getIntent().getStringExtra("pEntregador");
                gRecebedor = getIntent().getStringExtra("pRecebedor");
                gTipo = getIntent().getStringExtra("pTipo");
                gObservacao = getIntent().getStringExtra("pObservacao");

                //Convert image to string bit64
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                //Bitmap signatureBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte [] byte_arr = stream.toByteArray();
                imgSign = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                enviarProtocolo();

            }
        });

    }


    public void enviarProtocolo() {

        SendProtocolHttp sendProtocol = new SendProtocolHttp();
        sendProtocol.execute();

    }


    public class SendProtocolHttp extends AsyncTask<Void, Void , JSONObject> {


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignActivity.this,
                    "Enviando", "Aguarde enquanto o protocolo é enviado...", false, true);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            Log.i("LOG", "Iniciando Envio...");


            OutputStream outputStream = null;
            InputStream inputStream = null;
            HttpURLConnection httpConnection = null;

            try {

                URL url = new URL("http://exemplovar.esy.es/protocoloServer/server.php");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("documento", gDocumento);
                jsonObject.put("cliente", gCliente);
                jsonObject.put("data", gData);
                jsonObject.put("hora", gHora);
                jsonObject.put("tipo", gTipo);
                jsonObject.put("entregador", gEntregador);
                jsonObject.put("recebedor", gRecebedor);
                jsonObject.put("observacao", gObservacao);
                jsonObject.put("assinatura", imgSign);
                String message = jsonObject.toString();

                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setReadTimeout(10000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoInput(true);
                httpConnection.setDoOutput(true);
                httpConnection.setFixedLengthStreamingMode(message.getBytes().length);

                httpConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                outputStream = new BufferedOutputStream(httpConnection.getOutputStream());
                outputStream.write(message.getBytes());

                outputStream.flush();

                inputStream = httpConnection.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                try {

                    outputStream.close();
                    inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.i("LOG", "Envio Fianlizado");
            progressDialog.dismiss();

            //Restart Activity and clean screen
            Intent mainIntent = new Intent(SignActivity.this, MainActivity.class);
            SignActivity.this.startActivity(mainIntent);

          }

      }


    }



