package app.claudiomobiledev.protocolo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText eDocumento;
    private EditText eCliente;
    private EditText eData;
    private EditText eHora;
    private EditText eEntregador;
    private EditText eRecebedor;
    private EditText eObservacao;


    private String documento;
    private String cliente;
    private String data;
    private String hora;
    private String entregador;
    private String recebedor;
    private String observacao;
    private String tipo;
    private String imgStr;

    String TIPO1 = "Entrega";
    String TIPO2 = "Retirada";

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("Protocolo RioBike");
        setSupportActionBar(mToolbar);


        //Geting Current Date
        Date javaDate = new Date();
        DateFormat dfDate = new DateFormat();
        String date = (String) dfDate.format("yyyy-MM-dd", javaDate);
        //Getting Hour
        DateFormat dfHour = new DateFormat();
        String hour = (String) dfHour.format("HH:mm:ss", javaDate);


        //EditTexts
        eDocumento = (EditText) findViewById(R.id.doc_field);
        eCliente = (EditText) findViewById(R.id.cliente_field);
        eData = (EditText) findViewById(R.id.data_field);
        eHora = (EditText) findViewById(R.id.hora_field);
        eEntregador = (EditText) findViewById(R.id.entregador_field);
        eRecebedor = (EditText) findViewById(R.id.recebedor_field);
        eObservacao = (EditText) findViewById(R.id.obs_field);

        //Seting Text
        eData.setText(date);
        eHora.setText(hour);
        eEntregador.setText("Cláudio Luiz A Castro");

        RadioGroup group = (RadioGroup) findViewById(R.id.group1);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean entrega = R.id.radio_entrega == checkedId;
                boolean retirada = R.id.radio_retirada == checkedId;

                if(entrega){
                    tipo = TIPO1;
                }else if(retirada){
                    tipo = TIPO2;
                }
            }
        });


        Button buttonEnviar = (Button) findViewById(R.id.btn_enviar);
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                //Seting Strings
                documento = eDocumento.getText().toString();
                cliente = eCliente.getText().toString();
                data = eData.getText().toString();
                hora = eHora.getText().toString();
                entregador = eEntregador.getText().toString();
                recebedor = eRecebedor.getText().toString();
                observacao = eObservacao.getText().toString();


                if (isOnline()) {
                    Intent signIntent = new Intent(MainActivity.this, SignActivity.class);
                        signIntent.putExtra("pDocumento",getDocumento());
                        signIntent.putExtra("pCliente",getCliente());
                        signIntent.putExtra("pData",getData());
                        signIntent.putExtra("pHora",getHora());
                        signIntent.putExtra("pEntregador",getEntregador());
                        signIntent.putExtra("pRecebedor",getRecebedor());
                        signIntent.putExtra("pTipo",getTipo());
                        signIntent.putExtra("pObservacao",getObservacao());
                        MainActivity.this.startActivity(signIntent);

                } else {
                    Toast.makeText(getApplicationContext(), "Sem Conexão com a Internet. Tentar Novamente ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    //Getters and Setters
    public String getDocumento() {
        return documento;
    }

    public String getCliente() {
        return cliente;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getEntregador() {
        return entregador;
    }

    public String getRecebedor() {
        return recebedor;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getTipo() {
        return tipo;
    }



    // Method to Check Internet Connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
