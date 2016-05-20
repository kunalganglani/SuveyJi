package kunalganglani.com.suveyji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername, etPassword;
    // TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin= (Button) findViewById(R.id.bLogin);
        //   tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        bLogin.setOnClickListener(this);
        // tvRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String REGISTER_URL = "http://prakashupadhyay.com/SurveyApp/process.php";
        final ProgressDialog loading;
        loading = ProgressDialog.show(this,"Verifying...","Please wait...",false,false);
        final Intent intent = new Intent(this, Volunteer.class);

        switch (v.getId()){
            case R.id.bLogin:
            {
                if (etUsername.getText().toString().equals("admin") && (etPassword.getText().toString().equals("Welcome1") ))
                {
                    loading.dismiss();
                    startActivity(new Intent(this,Admin.class));
                }

                else
                {
                    String uname = etUsername.getText().toString();
                    String pwd = etPassword.getText().toString();

                    final JSONObject jSonObjData = new JSONObject();
                    try
                    {
                        JSONObject jsonVolObject = new JSONObject();
                        jsonVolObject.put("uname", uname);
                        jsonVolObject.put("pwd", pwd);
                        jSonObjData.put("action", "GetVolunteerDetails");
                        jSonObjData.put("data", jsonVolObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            REGISTER_URL, jSonObjData,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    loading.dismiss();
                                    System.out.println("\n\n Response from Backend:\n" + response);
                                    try
                                    {
                                        String jsonStr = response.getString("arrRes");
                                        JSONObject myjson = new JSONObject(jsonStr);
                                        if((Boolean)(myjson.get("userExistFlag"))==false)
                                        {
                                            Toast.makeText(Login.this, "Incorrect Credentials!", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            JSONArray volInfoArr = myjson.getJSONArray("volunteerDetails");

                                            int sizeVolArr = volInfoArr.length();
                                            //ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
                                            int vol_id=-1;
                                            JSONObject volJSONObject = new JSONObject();
                                            for (int i = 0; i < sizeVolArr; i++) {
                                                volJSONObject = volInfoArr.getJSONObject(i);
                                                //vol_id = Integer.parseInt(another_json_object.get("vol_id").toString());

                                                //arrays.add(volJSONObject);
                                            }


                                            //JSONArray volInfoArr = response.getJSONArray("arrRes");
                                            //int sizeVol =volInfoArr.length();
                                            //ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
                                            /*
                                            for (int i = 0; i < sizeVol; i++) {
                                                JSONObject another_json_object = volInfoArr.getJSONObject(i);
                                                arrays.add(another_json_object);
                                            }*/
                                            System.out.println(volJSONObject);
                                            System.out.println("ID of Vol: "+vol_id);
                                            intent.putExtra("volInfoStr",volJSONObject.toString() );
                                            startActivity(intent);
                                        }



                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }



                                    //startActivity(intent);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            System.out.println("\n\n Response from Backend:\n" + error.toString());
                        }
                    }) {

                        /**
                         * Passing some request headers
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }


                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(jsonObjReq);
                }


            }

            break;


            // case R.id.tvRegisterLink:
            //   startActivity(new Intent(this,Register.class));
            // break;

        }
    }


}
