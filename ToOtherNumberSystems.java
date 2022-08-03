package com.calculator.calcapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import static com.calculator.calcapp.MainActivity.*;

public class ToOtherNumberSystems extends AppCompatActivity implements View.OnClickListener
{
    EditText nextNumber, fromNS, toNS;
    TextView answer;
    Button btnConvert;
    ImageButton back, clear;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_other_number_systems);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toNS = findViewById(R.id.toNS);
        toNS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(ToOtherNumberSystems.this, "Число от 2 до 36.", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        fromNS = findViewById(R.id.fromNS);
        fromNS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(ToOtherNumberSystems.this, "Число от 2 до 36.", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        nextNumber = findViewById(R.id.nextNumber);
        nextNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (String.valueOf(nextNumber.getText()).equals("Введите число..."))
                    nextNumber.setText("");
                return false;
            }
        });
        back = findViewById(R.id.finish);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        answer = findViewById(R.id.answer);
        clear = findViewById(R.id.NSclear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(nextNumber.getText()).equals(""))
                    nextNumber.setText("Введите число...");
                else
                    nextNumber.setText("");
            }
        });
        btnConvert = findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(this);
        ImageButton swap = (ImageButton) findViewById(R.id.btnSwap);
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buf = String.valueOf(toNS.getText());
                toNS.setText( fromNS.getText() );
                fromNS.setText(buf);
            }
        });
    }

    public static String convert(int a, int d)
    {
        String res = "";
        while (a > 0){
            res = digits.charAt(a % d) + res;
            a /= d;
        }
        if (res.equals(""))
            res = "0";
        return res;
    }

    public static String toOtherNumberSystem(String str, int d1, int d2)
    {
        if (d1 == d2)
            return str;
        char z = '+';
        if (str.charAt(0) == '-'){
            str = str.substring(1);
            z = '-';
        }
        String fp = "0";
        if (isThere('.', str)){
            fp = "0" + str.substring(find('.', str), str.length());
            str = str.substring(0, find('.', str));
        }
        String num1 = convert(d1, d2), res = "0", k = "1";
        while (str.length() > 0)
        {
            String nextDigit = str.substring(str.length() - 1);
            nextDigit = convert(find(nextDigit.charAt(0), digits), d2);
            res = sum(res, fastProiz(nextDigit, k, d2), d2);
            str = str.substring(0, str.length() - 1);
            k = fastProiz(k, num1, d2);
        }
        if (fp.equals("0")) {
            if (z == '-')
                res = '-' + res;
            return res;
        }
        res += '.';
        String num2 = convert(d2, d1);
        int n = 0;
        while (!fp.equals("0") && n < accuracy){
            fp = fastProiz(fp, num2, d1);
            if (!isThere('.', fp))
                fp += ".0";
            String ip = fp.substring(0, find('.', fp));
            k = "1";
            String s = "0";
            while (ip.length() > 0){
                String nextDigit = ip.substring(ip.length() - 1);
                nextDigit = convert(find(nextDigit.charAt(0), digits), d2);
                s = sum(s, fastProiz(nextDigit, k, d2), d2);
                ip = ip.substring(0, ip.length() - 1);
                k = fastProiz(k, num1, d2);
            }
            fp = correct('0' + fp.substring(find('.', fp)));
            res += s;
            ++n;
        }
        if (z == '-' && !res.equals("0"))
            res = '-' + res;
        return res;
    }

    @Override
    public void onClick(View view)
    {
        String a = String.valueOf(fromNS.getText()), b = String.valueOf(toNS.getText());
        if (a.length() == 0 || b.length() == 0)
            return;
        for (int i = 0; i < a.length(); ++i)
            if (!isThere(a.charAt(i), digits)){
                Toast.makeText(ToOtherNumberSystems.this, "Обнаружены недопустимые символы!", Toast.LENGTH_LONG).show();
                return;
            }
        for (int i = 0; i < b.length(); ++i)
            if (!isThere(b.charAt(i), digits)){
                Toast.makeText(ToOtherNumberSystems.this, "Обнаружены недопустимые символы!", Toast.LENGTH_LONG).show();
                return;
            }
        a = correct(a);
        b = correct(b);
        if (a.length() > 2 || b.length() > 2 || a.equals("") || b.equals("") || a.equals("0") || b.equals("0")){
            Toast.makeText(ToOtherNumberSystems.this, "Не указана поддерживаемая система счисления...", Toast.LENGTH_LONG).show();
            return;
        }
        String num = String.valueOf(nextNumber.getText());
        num = upper(num);
        if (num.length() == 0){
            Toast.makeText(ToOtherNumberSystems.this, "Поле ввода пустое!", Toast.LENGTH_LONG).show();
            return;
        }
        int d1 = Integer.valueOf(a), d2 = Integer.valueOf(b), buf = accuracy;
        for (int i = 0; i < num.length(); ++i)
            if (!isThere(num.charAt(i), digits + '-') || find(num.charAt(i), digits) >= d1 && num.charAt(i) != '.'){
                String message = "Обнаружены недопустимые символы или цифры не из " + d1 + " системы счисления!";
                Toast.makeText(ToOtherNumberSystems.this, message, Toast.LENGTH_LONG).show();
                return;
            }
        if (d1 > 36 || d1 < 2 || d2 > 36 || d2 < 2){
            Toast.makeText(ToOtherNumberSystems.this, "Не указана поддерживаемая система счисления...", Toast.LENGTH_LONG).show();
            return;
        }
        accuracy = 100;
        num = toOtherNumberSystem(num, d1, d2);
        accuracy = buf;
        answer.setText(num);
    }
}
