package org.techtown.app_1_googleauth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    // Google api 클라이언트
    private GoogleSignInClient mGooglesSignInClient;

    // 구글계정
    private GoogleSignInAccount gsa;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth mAuth;

    // 구글 로그인 버튼 및 로그아웃버튼
    private SignInButton btnGoogleLogin;
    private Button btnLogoutGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //파이어베이스 인증 객체 선언
        mAuth = FirebaseAuth.getInstance();

        // google 로그인을 앱에 통합
        // GoogleSignInOptions객체를 구성할 때 requestIdToken을 호출

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail().build();

        mGooglesSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btnGoogleLogin = findViewById(R.id.btn_google_sign_in);
        btnGoogleLogin.setOnClickListener(view -> {
            // 기존에 로그인 했던 계정을 확인한다
            gsa = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
            if (gsa != null) { // 로그인 되어있을경우
                Toast.makeText(MainActivity.this, R.string.status_login, Toast.LENGTH_SHORT).show();
            }else {
                signIn();
            }
        });
        btnLogoutGoogle = findViewById(R.id.btn_logout_google);
        btnLogoutGoogle.setOnClickListener(view -> {
            signOut(); // 로그아웃
        });
    }
    private void signIn(){ // 들어가서 할일 여기 적으면 됨 -> 이때 다른 화면으로 옮기기 가능?
        Intent signInIntent = mGooglesSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        mGooglesSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, R.string.success_logout, Toast.LENGTH_SHORT).show();
                    // ...
                });
        gsa = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        }
    }

}