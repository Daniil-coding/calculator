package com.calculator.calcapp;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import static com.calculator.calcapp.ToOtherNumberSystems.toOtherNumberSystem;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static EditText numberSystem, input, setAccuracy;
    public static String str = "", result = "";
    public final static String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ.,П";
    public static int number = 1, accuracy = 10;
    public final static String message1 = "Введите основание системы счисления! (число от 2 до 36)";
    public final static String message2 = "Введена не та система счисления, обнаружены посторонние символы или непрвильная скобочная последовательность!";
    public final static String message3 = "Этот калькулятор не считает факториал дробного числа.";
    public final static String PI = "3.1415926535897932384626433832795"; // 0288419716939937510582097494459230781640628620899862803482534211706798
    public final static String E = "0.000000001";
    public static boolean RAD = true, keyboard_plug = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAccuracy = findViewById(R.id.accuracy);
        setAccuracy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(MainActivity.this, "От 3 до 100 цифр после точки.", Toast.LENGTH_SHORT ).show();
                return false;
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        input = findViewById(R.id.input);
        input.setInputType(InputType.TYPE_NULL);

        input.setTextIsSelectable(true);
        numberSystem = findViewById(R.id.numberSystem);
        numberSystem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(MainActivity.this, "Число от 2 до 36.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        Button sin = findViewById(R.id.sin);
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("sin(");
                else if (isThere(s.charAt(s.length() - 1), digits + "!)"))
                    input.setText(s + "×sin(");
                else
                    input.setText(s + "sin(");
                input.setSelection(input.getText().length());
            }
        });
        Button cos = findViewById(R.id.cos);
        cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("cos(");
                else if (isThere(s.charAt(s.length() - 1), digits + "!)"))
                    input.setText(s + "×cos(");
                else
                    input.setText(s + "cos(");
                input.setSelection(input.getText().length());
            }
        });
        Button tg = findViewById(R.id.tg);
        tg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("tg(");
                else if (isThere(s.charAt(s.length() - 1), digits + "!)"))
                    input.setText(s + "×tg(");
                else
                    input.setText(s + "tg(");
                input.setSelection(input.getText().length());
            }
        });
        Button ctg = findViewById(R.id.ctg);
        ctg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("ctg(");
                else if (isThere(s.charAt(s.length() - 1), digits + "!)"))
                    input.setText(s + "×ctg(");
                else
                    input.setText(s + "ctg(");
                input.setSelection(input.getText().length());
            }
        });
        ImageButton numberSystems = (ImageButton) findViewById(R.id.toOtherNumberSystems);
        numberSystems.setOnClickListener(this);
        Button ip = findViewById(R.id.integerPart);
        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0) {
                    input.setText("[");
                    return;
                }
                if (s.length() > 1 && isThere(s.charAt(s.length() - 2), "sincotg")) {
                    input.setText(s + '[');
                    return;
                }
                char sim = s.charAt(s.length() - 1);
                if (s.length() > 0) {
                    if (isThere(sim, "([<{+-×*/^√"))
                        s += '[';
                    else if (isThere(sim, ">}])!" + digits) && canCloseBracket(s, ']'))
                        s += ']';
                    else if (isThere(sim, digits + "}])!"))
                        s += "×[";
                    else
                        s += ']';
                    input.setText(s);
                }
                else
                    input.setText("[");
                input.setSelection(input.getText().length());
            }
        });
        Button fp = findViewById(R.id.floatPart);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0) {
                    input.setText("{");
                    return;
                }
                if (s.length() > 1 && isThere(s.charAt(s.length() - 2), "sincotg")) {
                    input.setText(s + '{');
                    return;
                }
                char sim = s.charAt(s.length() - 1);
                if (s.length() > 0) {
                    if (isThere(sim, "<([{+-×*/^√"))
                        s += '{';
                    else if (isThere(sim, "}])!" + digits) && canCloseBracket(s, '}'))
                        s += '}';
                    else if (isThere(sim, digits + "}])!"))
                        s += "×{";
                    else
                        s += '}';
                    input.setText(s);
                }
                else
                    input.setText("{");
                input.setSelection(input.getText().length());
            }
        });
        Button brackets = findViewById(R.id.brackets);
        brackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0) {
                    input.setText("(");
                    return;
                }
                if (s.length() > 1 && isThere(s.charAt(s.length() - 2), "sincotg")) {
                    input.setText(s + '(');
                    return;
                }
                char sim = s.charAt(s.length() - 1);
                if (s.length() > 0) {
                    if (isThere(sim, "<([{+-×*/^√"))
                        s += '(';
                    else if (isThere(sim, "}])!" + digits) && canCloseBracket(s, ')'))
                        s += ')';
                    else if (isThere(sim, digits + "}])!"))
                        s += "×(";
                    else
                        s += ')';
                    input.setText(s);
                }
                else
                    input.setText("(");
                input.setSelection(input.getText().length());
            }
        });
        Button absStick = findViewById(R.id.absStick);
        absStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("|");
                else
                    input.setText(s + "|");
                input.setSelection(input.getText().length());
            }
        });
        Button pi = findViewById(R.id.pi);
        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = String.valueOf(numberSystem.getText());
                if (num.length() > 2)
                    return;
                for (int i = 0; i < num.length(); ++i)
                    if (find(num.charAt(i), digits) == -1)
                        Toast.makeText(MainActivity.this, message1, Toast.LENGTH_LONG).show();
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("П");
                else if (isThere(s.charAt(s.length() - 1), digits + "!)"))
                    input.setText(s + "×П");
                else
                    input.setText(s + "П");
                input.setSelection(input.getText().length());
            }
        });
        Button zero = findViewById(R.id.zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                input.setText(s + "0");
                input.setSelection(input.getText().length());
            }
        });
        Button one = findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "1");
                input.setSelection(input.getText().length());
            }
        });
        Button two = findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "2");
                input.setSelection(input.getText().length());
            }
        });
        Button three = findViewById(R.id.three);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "3");
                input.setSelection(input.getText().length());
            }
        });
        Button four = findViewById(R.id.four);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "4");
                input.setSelection(input.getText().length());
            }
        });
        Button five = findViewById(R.id.five);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "5");
                input.setSelection(input.getText().length());
            }
        });
        Button six = findViewById(R.id.six);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "6");
                input.setSelection(input.getText().length());
            }
        });
        Button seven = findViewById(R.id.seven);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "7");
                input.setSelection(input.getText().length());
            }
        });
        Button eight = findViewById(R.id.eight);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input.setText(input.getText() + "8");
                input.setSelection(input.getText().length());
            }
        });
        Button nine = findViewById(R.id.nine);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "9");
                input.setSelection(input.getText().length());
            }
        });
        Button plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "+");
                input.setSelection(input.getText().length());
            }
        });
        Button minus = findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "-");
                input.setSelection(input.getText().length());
            }
        });
        Button division = findViewById(R.id.division);
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "/");
                input.setSelection(input.getText().length());
            }
        });
        Button proiz = findViewById(R.id.proiz);
        proiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "×");
                input.setSelection(input.getText().length());
            }
        });
        Button dot = findViewById(R.id.dot);
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = String.valueOf(input.getText());
                if (s.length() == 0) {
                    input.setText("0,");
                    return;
                }
                char sim = s.charAt(s.length() - 1);
                if (isThere(sim, "]})"))
                    s += "×0,";
                else if (!isThere(sim, digits))
                    s += "0,";
                else if (!isThere(sim, ",.П"))
                    s += ',';
                input.setText(s);
                input.setSelection(input.getText().length());
            }
        });
        Button sqrt = findViewById(R.id.sqrt);
        sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = String.valueOf(numberSystem.getText());
                if (num.length() > 2)
                    return;
                for (int i = 0; i < num.length(); ++i)
                    if (find(num.charAt(i), digits) == -1)
                        Toast.makeText(MainActivity.this, message1, Toast.LENGTH_LONG).show();
                String s = String.valueOf(input.getText());
                if (s.length() == 0)
                    input.setText("√");
                else if (isThere(s.charAt(s.length() - 1), digits + "!)"))
                    input.setText(s + "×√");
                else
                    input.setText(s + "√");
                input.setSelection(input.getText().length());
            }
        });
        Button factorial = findViewById(R.id.factorial);
        factorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.valueOf(input.getText()), buf = "";
                while (str.length() > 0 && isThere(str.charAt(str.length() - 1), digits)) {
                    buf += str.charAt(str.length() - 1);
                    str = str.substring(0, str.length() - 1);
                }
                if (isThere('.', buf)){
                    Toast.makeText(MainActivity.this, message3, Toast.LENGTH_LONG).show();
                    return;
                }
                input.setText(input.getText() + "!");
                input.setSelection(input.getText().length());
            }
        });
        Button pow = findViewById(R.id.pow);
        pow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText(input.getText() + "^");
                input.setSelection(input.getText().length());
            }
        });
        ImageButton backspace = findViewById(R.id.backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.valueOf(input.getText());
                if (str.length() > 0) {
                    str = str.substring(0, str.length() - 1);
                    input.setText(str);
                }
                input.setSelection(input.getText().length());
            }
        });
        final ImageButton keyboard = findViewById(R.id.keyboardSwitch);
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyboard_plug) {
                    input.setInputType(InputType.TYPE_NULL);
                    keyboard.setImageResource(R.drawable.keyboard);
                } else {
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    keyboard.setImageResource(R.drawable.keyboard_plugged);
                }
                keyboard_plug = !keyboard_plug;
            }
        });
        Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText("");
            }
        });
        final Button radOrDeg = findViewById(R.id.switchRadOrDeg);
        radOrDeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RAD)
                    radOrDeg.setText("DEG:");
                else
                    radOrDeg.setText("RAD:");
                RAD = !RAD;
            }
        });
        Button solution = findViewById(R.id.solution);
        solution.setOnClickListener(this);
        ImageButton equally = findViewById(R.id.equally);
        equally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = String.valueOf(input.getText());
                    str = calculateFormat(str);
                    str = absDetection(str);
                    try {
                        accuracy = Integer.valueOf(String.valueOf(setAccuracy.getText()));
                    } catch (Exception e) { accuracy = 10;}
                    int base = Integer.valueOf(String.valueOf(numberSystem.getText()));
                    for (int i = 0; i < str.length(); i++) {
                        if (str.charAt(i) - '0' >= base) {
                            Toast.makeText(MainActivity.this, "В этой системе счисления нет таких цифр.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    String answer = value(str, base);
                    answer = readFormat(answer);
                    input.setText(answer);
                    if (String.valueOf(input.getText()).equals("error")) {
                        input.setText(str);
                        Toast.makeText(MainActivity.this, "Что-то пошло не так...\nПопробуйте запросить решение.",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Что-то пошло не так...\nПопробуйте запросить решение.",
                                   Toast.LENGTH_LONG).show();
                }
                number = 1;
            }
        });
    }

    private static boolean canCloseBracket(final String s, char bracket)
    {
        String stack = "" + bracket;
        int i = s.length();
        while (i-- > 0) {
            char sim = s.charAt(i);
            if (isThere(sim, ")]}"))
                stack += sim;
            else if (isThere(sim, "([{")) {
                char stackTop = stack.charAt(stack.length() - 1);
                if (sim == '(' && stackTop == ')' ||
                    sim == '[' && stackTop == ']' ||
                    sim == '{' && stackTop == '}')
                        stack = stack.substring(0, stack.length() - 1);
                else
                    return false;
            }
            if (stack.length() == 0)
                return true;
        }
        return false;
    }

    public static boolean corBrackSeq(final String str)
    {
        int b = 0;
        String s = "";
        for (int i = 0; i < str.length(); ++i)
        {
            char sim = str.charAt(i);
            if (isThere(sim, "([{<")) {
                b++;
                s += sim;
            }
            else if (isThere(sim, ")]}>")){
                b--;
                if (s.length() == 0)
                    return false;
                char prev = s.charAt(s.length() - 1);
                if (!(prev == '(' && sim == ')' || prev == '[' && sim == ']' || prev == '{' && sim == '}' || prev == '<' && sim == '>'))
                    return false;
                else{
                    s = s.substring(0, s.length() - 1);
                }
            }
            if (b < 0)
                return false;
        }
        return b == 0 && s.length() == 0;
    }

    public static boolean isThere(char sim, final String str)
    {
        for (int i = 0; i < str.length(); ++i)
            if (str.charAt(i) == sim)
                return true;
        return false;
    }

    public static int find(char sim, final String str)
    {
        int result = 0;
        while (result != str.length() && str.charAt(result) != sim)
            result++;
        if (result == str.length())
            return -1;
        else
            return result;
    }

    public static int toNumber(String s)
    {
        s = correct(s);
        int pos = 0;
        while (pos < s.length() - 1 && isThere(s.charAt(pos), digits))
            ++pos;
        if (pos == s.length())
            return -1;
        int result = 0;
        while (s.length() > 0){
            result = result * 10 + find(s.charAt(0), digits);
            s = s.substring(1, s.length());
        }
        if (result < 2 || result > 36)
            return -1;
        return result;
    }

    public static String correct(String num)
    {
        if (num.length() == 0)
            return null;
        char z = '+';
        if (num.charAt(0) == '-'){
            z = '-';
            num = num.substring(1, num.length());
        }
        if (isThere('.', num))
            while(num.charAt(num.length() - 1) == '0')
                num = num.substring(0, num.length() - 1);
        if (num.charAt(num.length() - 1) == '.')
            num = num.substring(0, num.length() - 1);
        while (num.length() > 0 && num.charAt(0) == '0')
            num = num.substring(1, num.length());
        if (num.equals("") || num.charAt(0) == '.')
            num = '0' + num;
        if (z == '-' && !num.equals("0"))
            num = '-' + num;
        return num;
    }

    public static int moreAbs(String a, String b)
    {
        if (a.charAt(0) == '-')
            a = a.substring(1, a.length());
        if (b.charAt(0) == '-')
            b = b.substring(1, b.length());
        if (!isThere('.', a))
            a += '.';
        if (!isThere('.', b))
            b += '.';
        while (find('.', a) < find('.', b))
            a = '0' + a;
        while (find('.', a) > find('.', b))
            b = '0' + b;
        while (a.length() < b.length())
            a += '0';
        while (a.length() > b.length())
            b += '0';
        int i = 0;
        while (i != a.length() && a.charAt(i) == b.charAt(i))
            ++i;
        if (i == a.length())
            return 0;
        if (a.charAt(i) > b.charAt(i))
            return 1;
        return -1;
    }

    public static String sum(String a, String b, int d)
    {
        char z1 = '+', z2 = '+';
        if (a.charAt(0) == '-'){
            a = a.substring(1);
            z1 = '-';
        }
        if (b.charAt(0) == '-'){
            b = b.substring(1);
            z2 = '-';
        }
        if (!isThere('.', a))
            a += '.';
        if (!isThere('.', b))
            b += '.';
        while (find('.', a) > find('.', b))
            b = '0' + b;
        while (find('.', a) < find('.', b))
            a = '0' + a;
        while (a.length() > b.length())
            b += '0';
        while (a.length() < b.length())
            a += '0';
        String result = "";
        int buf = 0;
        if (z1 == z2)
        {
            while (a.length() > 0)
            {
                if (a.charAt(a.length() - 1) == '.'){
                    a = a.substring(0, a.length() - 1);
                    b = b.substring(0, b.length() - 1);
                    result = '.' + result;
                    continue;
                }
                char x = a.charAt(a.length() - 1);
                char y = b.charAt(b.length() - 1);
                a = a.substring(0, a.length() - 1);
                b = b.substring(0, b.length() - 1);
                buf += find(x, digits) + find(y, digits);
                result = digits.charAt(buf % d) + result;
                buf /= d;
            }
            result = digits.charAt(buf) + result;
            if (z1 == '-')
                result = '-' + result;
            return correct(result);
        }
        else
        {
            if (moreAbs(a, b) < 0) {
                String str = a;
                a = b;
                b = str;
                char sim = z1;
                z1 = z2;
                z2 = sim;
            }
            while (a.length() > 0){
                if (a.charAt(a.length() - 1) == '.'){
                    a = a.substring(0, a.length() - 1);
                    b = b.substring(0, b.length() - 1);
                    result = '.' + result;
                    continue;
                }
                char x = a.charAt(a.length() - 1);
                char y = b.charAt(b.length() - 1);
                a = a.substring(0, a.length() - 1);
                b = b.substring(0, b.length() - 1);
                buf = find(x, digits) - find(y, digits);
                if (buf < 0){
                    buf = d + find(x, digits) - find(y, digits);
                    int i = a.length() - 1;
                    while (a.charAt(i) == '0' || a.charAt(i) == '.') {
                        if (a.charAt(i) != '.')
                            a = a.substring(0, i) + digits.charAt(d - 1) + a.substring(i + 1);
                        --i;
                    }
                    char num = a.charAt(i);
                    num = digits.charAt( find(num, digits) - 1 );
                    a = a.substring(0, i) + num + a.substring(i + 1);
                }
                result = digits.charAt(buf) + result;
            }
            if (z1 == '-')
                result = '-' + result;
            return correct(result);
        }
    }

    public static String fastProiz(String a, String b, int d)
    {
        a = correct(a);
        b = correct(b);
        char z1 = '+', z2 = '+';
        if (a.charAt(0) == '-') {
            a = a.substring(1);
            z1 = '-';
        }
        if (b.charAt(0) == '-') {
            b = b.substring(1);
            z2 = '-';
        }
        int fp = 0;
        if (!isThere('.', a))
            a += '.';
        fp += a.length() - find('.', a) - 1;
        if (!isThere('.', b))
            b += '.';
        fp += b.length() - find('.', b) - 1;
        int index = find('.', a);
        a = a.substring(0, index) + a.substring(index + 1);
        index = find('.', b);
        b = b.substring(0, index) + b.substring(index + 1);
        if (a.length() + b.length() < 30) {
            String result = integerProiz(a, b, d);
            result = fractionPull(result, fp);
            if (z1 != z2)
                result = '-' + result;
            return result;
        }
        if (a.length() < b.length()) {
            int k = b.length() - a.length();
            String buf = "";
            for (int i = 0; i < k; i++)
                buf += '0';
            buf += a;
            a = buf;
        }
        if (b.length() < a.length()) {
            int k = a.length() - b.length();
            String buf = "";
            for (int i = 0; i < k; i++)
                buf += '0';
            buf += b;
            b = buf;
        }
        int n = max(a.length(), b.length());
        n /= 2;
        String a1 = a.substring(0, a.length() - n);
        String b1 = b.substring(0, b.length() - n);
        String a0 = a.substring(a.length() - n);
        String b0 = b.substring(b.length() - n);
        String x = fastProiz(a1, b1, d);
        String y = fastProiz(a0, b0, d);
        String z = fastProiz( sum(a0, a1, d), sum(b0, b1, d), d );
        String c = sum( z, '-' + sum(x, y, d), d );
        x = fractionPush(x, 2 * n);
        c = fractionPush(c, n);
        String result = sum( x, sum(c, y, d), d );
        result = fractionPull(result, fp);
        if (z1 != z2)
            result = '-' + result;
        return result;
    }

    private static String integerProiz(String a, String b, int d)
    {
        /*char z1 = '+', z2 = '+';
        if (a.charAt(0) == '-'){
            a = a.substring(1);
            z1 = '-';
        }
        if (b.charAt(0) == '-'){
            b = b.substring(1);
            z2 = '-';
        }
        a = correct(a);
        b = correct(b);
        if (!isThere('.', a))
            a += '.';
        if (!isThere('.', b))
            b += '.';
        int fp = a.length() - find('.', a) + b.length() - find('.', b) - 2;
        a = a.substring(0, find('.', a)) + a.substring(find('.', a) + 1);
        b = b.substring(0, find('.', b)) + b.substring(find('.', b) + 1);*/
        String result = "0";
        int buf = 0, n = 0;
        b = '0' + b;
        for (int i = a.length() - 1; i >= 0; --i){
            char x = a.charAt(i);
            String num = "";
            for (int j = b.length() - 1; j >= 0; --j) {
                char y = b.charAt(j);
                buf += find(y, digits) * find(x, digits);
                num = digits.charAt(buf % d) + num;
                buf /= d;
            }
            for (int j = 0; j < n; ++j)
                num += '0';
            ++n;
            result = sum(result, num, d);
        }
        /*result = fractionPull(result, fp);
        if (z1 != z2)
            result = '-' + result;*/
        return correct(result);
    }

    public static String fractionPush(String a, int n)
    {
        if (!isThere('.', a))
            a += ".";
        int index = find('.', a);
        String ip = a.substring(0, index);
        String fp = a.substring(index + 1);
        int k = n - fp.length();
        for (int i = 0; i < k; i++)
            fp += '0';
        String result = ip;
        result += fp.substring(0, n);
        result += '.';
        result += fp.substring(n);
        return correct(result);
    }

    public static String fractionPull(String a, int n)
    {
        char z = '+';
        if (a.charAt(0) == '-') {
            z = '-';
            a = a.substring(1);
        }
        if (!isThere('.', a))
            a += ".";
        int index = find('.', a);
        String ip = a.substring(0, index);
        String fp = a.substring(index + 1);
        String buf = "";
        int k = n - ip.length();
        for (int i = 0; i <= k; i++)
            buf += '0';
        ip = buf + ip;
        k = ip.length() - n;
        String result = "";
        if (z == '-')
            result += '-';
        result += ip.substring(0, k);
        result += '.';
        result += ip.substring(k);
        result += fp;
        return correct(result);
    }

    public static String division(String a, String b, int d) throws ArithmeticException
    {
        String result = "", num;
        a = correct(a);
        b = correct(b);
        while (a.length() > 1 && b.length() > 1 &&
                a.charAt(a.length() - 1) == b.charAt(b.length() - 1) &&
                a.charAt(a.length() - 1) == '0'){
            a = a.substring(0, a.length() - 1);
            b = b.substring(0, b.length() - 1);
        }
        char z1 = '+', z2 = '+';
        if (a.charAt(0) == '-'){
            z1 = '-';
            a = a.substring(1);
        }
        if (b.charAt(0) == '-'){
            z2 = '-';
            b = b.substring(1);
        }
        if (b.equals("1")){
            if (z1 == z2)
                return a;
            return '-' + a;
        }
        if (b.equals("0"))
            throw new ArithmeticException("На 0 делить нельзя!");
        if (a.equals("0"))
            return "0";
        if (!isThere('.', a))
            a += '.';
        if (!isThere('.', b))
            b += '.';
        int k = max(a.length() - find('.', a) - 1, b.length() - find('.', b) - 1);
        a = fractionPush(a, k);
        b = fractionPush(b, k);
        accuracy += 1;
        while ((!a.equals("0") && (!isThere('.', result) || result.length() - find('.', result) < accuracy + 1))
                || zero(result))
        {
            num = "0";
            String add = "1", current = b;
            int delta = a.length() - b.length();
            for (int i = 0; i < delta; i++) {
                current = fractionPush(current, 1);
                add += '0';
            }
            while (!add.equals("")) {
                while (moreAbs(a, current) > -1) {
                    num = sum(num, add, d);
                    a = sum(a, '-' + current, d);
                }
                add = add.substring(0, add.length() - 1);
                current = fractionPull(current, 1);
            }
            if (!a.equals("0")) {
                if (!isThere('.', result))
                    num += '.';
                a += "0";
            }
            result += num;
        }
        accuracy -= 1;
        if (z1 != z2)
            result = '-' + result;
        if (!isThere('.', result) || result.length() - find('.', result) - 1 <= accuracy)
            return result;
        char lastDigit = result.charAt(result.length() - 1);
        if (find(lastDigit, digits) >= d / 2) {
            int i = result.length() - 2;
            while (result.charAt(i) != '.' && find(result.charAt(i), digits) == d - 1)
                i--;
            if (result.charAt(i) == '.') {
                result = result.substring(0, i);
                result = sum(result, "1", d);
            }
            else {
                lastDigit = result.charAt(i);
                lastDigit += 1;
                result = result.substring(0, i) + (lastDigit + "");
            }
        }
        return correct(result);
    }

    private static String applyPeriod(String a, String t, int d)
    {
        char z = '+';
        if (a.charAt(0) == '-') {
            z = '-';
            a = a.substring(1);
        }
        if (moreAbs(a, t) == -1)
            return (z == '-' ? '-' + a : a);
        t = '-' + t;
        int n = 0;
        while (moreAbs(a, t) == 1) {
            t = fractionPush(t, 1);
            n++;
        }
        if (moreAbs(a, t) == -1) {
            t = fractionPull(t, 1);
            n--;
        }
        while (n >= 0) {
            while (moreAbs(a, t) > -1)
                a = sum(a, t, d);
            t = fractionPull(t, 1);
            n--;
        }
        if (z == '-')
            a = '-' + a;
        return a;
    }

    public static String factorial(String a, int d)
    {
        a = correct(a);
        if (isThere('.', a))
            return "error";
        String result = "1";
        while (!a.equals("0")){
            result = fastProiz(result, a, d);
            a = sum(a, "-1", d);
        }
        return result;
    }

    public static String round(String a, int d)
    {
        char z = '+';
        if (a.charAt(0) == '-'){
            z = '-';
            a = a.substring(1, a.length());
        }
        if (!isThere('.', a))
            a += '.';
        String ip = a.substring(0, find('.', a));
        String num = sum("1", ip, d);
        if (moreAbs(sum(num, '-' + a, d), E) <= 0) {
            if (z == '-')
                return '-' + num;
            return num;
        }
        if (moreAbs(sum(a, '-' + ip, d), E) <= 0) {
            if (z == '+')
                return ip;
            return '-' + ip;
        }
        if (z == '-')
            return '-' + a;
        return a;
    }

    public static String pow(final String a, final String b, int d)
    {
        if (b.equals("0")){
            if (a.equals("0"))
                return "error";
            return "1";
        }
        if (a.equals("0") && b.charAt(0) == '-')
            return "error";
        if (a.equals("0"))
            return "0";
        if (b.charAt(0) == '-') {
            String ans = "";
            try {
                accuracy = 100;
                ans = division("1", pow(a, b.substring(1), d), d);} catch(Exception e){}
                accuracy = Integer.valueOf(String.valueOf(setAccuracy.getText()));
            return ans;
        }
        if (a.equals("1"))
            return "1";
        if (find(b.charAt(b.length() - 1), digits) % 2 == 1)
            return fastProiz(pow(a, sum(b, "-1", d), d), a, d);
            String x = "2";
            if (d == 2)
                x = "10";
            try{ x = division(b, x, d); } catch (Exception e) {}
            String ans =  pow(a, x, d);
            return fastProiz(ans, ans, d);
    }

    public static String upper(final String s)
    {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            char sim = s.charAt(i);
            if (sim >= 'a' && sim <= 'z')
                sim += 'A' - 'a';
            result += sim;
        }
        return result;
    }

    public String value(String s, int d)
    {
        while (isThere('П', s)){
            int pos = find('П', s);
            s = s.substring(0, pos) + toOtherNumberSystem(PI, 10, d) + s.substring(pos + 1);
        }
        while (isThere('(', s)) {
            int beg = find('(', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '(')
                    ++b;
                else if (s.charAt(end) == ')')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String val = value(num, d);
            if (beg > 0 && s.charAt(beg - 1) == 'n') {
                String buf = sin(val, d);
                s = s.substring(0, beg - 3) + buf + s.substring(end + 1);
                result += number + ") sin(" + val + ") = " + buf + '\n';
                number++;
            }
            else if (beg > 0 && s.charAt(beg - 1) == 's') {
                String buf = cos(val, d);
                s = s.substring(0, beg - 3) + buf + s.substring(end + 1);
                result += number + ") cos(" + val + ") = " + buf + '\n';
                number++;
            }
            else if (beg > 2 && s.substring(beg - 3, beg).equals("ctg")) {
                String buf = round(ctg(val, d), d);
                s = s.substring(0, beg - 3) + buf + s.substring(end + 1);
                result += number + ") ctg(" + val + ") = " + buf + '\n';
                number++;
            }
            else if (beg > 1 && s.substring(beg - 2, beg).equals("tg")) {
                String buf = round(tg(val, d), d);
                s = s.substring(0, beg - 2) + round(tg(val, d), d) + s.substring(end + 1);
                result += number + ") tg(" + val + ") = " + buf + '\n';
                number++;
            }
            else
                s = s.substring(0, beg) + val + s.substring(end + 1);
        }
        while (isThere('[', s)) {
            int beg = find('[', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '[')
                    ++b;
                else if (s.charAt(end) == ']')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String val = value(num, d);
            result += number + ") " + '[' + val + "] = ";
            if (isThere('.', val)) {
                if (val.charAt(0) != '-')
                    val = val.substring(0, find('.', val));
                else
                    val = sum(val.substring(0, find('.', val)), "-1", d);
            }
            result += val + '\n';
            s = s.substring(0, beg) + val + s.substring(end + 1, s.length());
            number++;
        }
        while (isThere('{', s)) {
            int beg = find('{', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '{')
                    ++b;
                else if (s.charAt(end) == '}')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String val = value(num, d);
            result += number + ") " + '{' + val + "} = ";
            if (!isThere('.', val))
                val = "0";
            else if (val.charAt(0) != '-')
                val = "0." + val.substring(find('.', val) + 1, val.length());
            else {
                String intPart = sum(val.substring(0, find('.', val)), "-1", d);
                val = sum(val, intPart.substring(1, intPart.length()), d);
            }
            result += val + '\n';
            s = s.substring(0, beg) + val + s.substring(end + 1, s.length());
            number++;
        }
        while (isThere('<', s)) {
            int beg = find('<', s), b = 1, end = beg;
            while (b != 0) {
                end++;
                if (s.charAt(end) == '<')
                    ++b;
                else if (s.charAt(end) == '>')
                    --b;
            }
            String num = s.substring(beg + 1, end);
            String res = value(num, d);
            if (res.charAt(0) == '-')
                res = res.substring(1, res.length());
            s = s.substring(0, beg) + res + s.substring(end + 1, s.length());
        }
        while (isThere('!', s)) {
            int end = find('!', s), beg = end;
            while (beg > 0 && isThere(s.charAt(beg - 1), digits))
                beg--;
            String num = factorial(s.substring(beg, end), d);
            result += number + ") " +  s.substring(beg, end + 1) + " = " + num + '\n';
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        while (isThere('^', s)) {
            int pos = find('^', s), beg = pos, end = beg;
            while (beg > 0 && isThere(s.charAt(beg - 1), digits))
                beg--;
            if (beg == 1 && s.charAt(0) == '-')
                beg--;
            if (s.charAt(end + 1) == '-')
                ++end;
            while (end < s.length() - 1 && isThere(s.charAt(end + 1), digits))
                end++;
            result += number + ") " + s.substring(beg, end + 1) + " = ";
            String a = s.substring(beg, pos);
            String b = s.substring(find('^', s) + 1, end + 1);
            String num = pow(a, b, d);
            result += num + '\n';
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        while (isThere('√', s)) {
            int i = 0;
            while (s.charAt(i) != '√')
                i++;
            int j = i + 1;
            if (s.charAt(i + 1) == '-' || s.charAt(i + 1) == '+')
                j++;
            while (j < s.length() && isThere(s.charAt(j), digits))
                j++;
            String num = s.substring(i, j);
            int index = 0;
            while (!isThere(num.charAt(index), digits) && num.charAt(index) != '-')
                index++;
            num = num.substring(index);
            String val = round(sqrt(num, d), d);
            result += number + ") √(" + num + ") = " + val + '\n';
            s = s.substring(0, i) + val + s.substring(j);
            number++;
        }
        while (isThere('×', s) || isThere('/', s)) {
            int pos = 0;
            while (s.charAt(pos) != '×' && s.charAt(pos) != '/')
                ++pos;
            int beg = pos, end = pos;
            while (beg > 0 && isThere(s.charAt(beg - 1), digits))
                --beg;
            if (beg == 1 && s.charAt(0) == '-')
                --beg;
            if (s.charAt(end + 1) == '-')
                ++end;
            while (end < s.length() - 1 && isThere(s.charAt(end + 1), digits))
                ++end;
            String a = s.substring(beg, pos);
            String b = s.substring(pos + 1, end + 1);
            String num;
            if (s.charAt(pos) == '×')
                num = fastProiz(a, b, d);
            else try {
                a = correct(a); b = correct(b);
                num = division(a, b, d);
            } catch (ArithmeticException e) {
                    return "error";
                }
            if (b.charAt(0) == '-')
                result += number + ") " + a + " " + s.charAt(pos) + " (" + b + ") = " + num + '\n';
            else
                result += number + ") " + a + " " + s.charAt(pos) + " " + b + " = " + num + '\n';
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        while (isThere('+', s) || isThere('-', s.substring(1))) {
            int pos = 1;
            while (s.charAt(pos) != '+' && s.charAt(pos) != '-')
                ++pos;
            int beg = pos, end = pos;
            while (beg > 0 && isThere(s.charAt(beg - 1), digits))
                --beg;
            if (beg == 1 && s.charAt(0) == '-')
                --beg;
            if (s.charAt(end + 1) == '-')
                ++end;
            while (end < s.length() - 1 && isThere(s.charAt(end + 1), digits))
                ++end;
            String a = s.substring(beg, pos);
            String b = s.substring(pos + 1, end + 1);
            String num;
            if (s.charAt(pos) == '-')
            {
                if (b.charAt(0) == '-') {
                    result += number + ") " + a + " - (" + b + ") = ";
                    b = b.substring(1);
                }
                else {
                    result += number + ") " + a + " - " + b + " = ";
                    b = '-' + b;
                }
            }
            else{
                if (b.charAt(0) == '-')
                    result += number + ") " + a + " + (" + b + ") = ";
                else
                    result += number + ") " + a + " + " + b + " = ";
            }
            num = sum(a, b, d);
            result +=  num + '\n';
            s = s.substring(0, beg) + num + s.substring(end + 1);
            number++;
        }
        return correct(s);
    }

    public static String sin(String a, int d)
    {
        a = toOtherNumberSystem(correct(a), d, 10);
        if (!RAD)
            a = toRadians(a);
        a = applyPeriod(a, fastProiz("2", PI, 10), 10);
        double val = Double.valueOf(a);
        val = Math.sin(val);
        if (abs(val - 0.5) <= 1e-10)
            val = 0.5;
        else if (abs(val + 0.5) <= 1e-10)
            val = -0.5;
        else if (abs(val) <= 1e-10)
            val = 0;
        String ans =  correct(String.valueOf(val));
        if (isThere('E', ans)) {
            int p = find('E', ans);
            int n = abs(Integer.valueOf(ans.substring(p + 1, ans.length())));
            ans = ans.substring(0, p);
            ans = fractionPull(ans, n);
        }
        ans = toOtherNumberSystem(ans, 10, d);
        return ans;
    }

    public static String sqrt(String x, int d)
    {
        int buf = accuracy;
        accuracy = 10;
        x = correct(x);
        if (x.charAt(0) == '-')
            return "error";
        if (isThere('.', x)) {
            int index = find('.', x);
            int n = x.length() - index - 1;
            if (n > accuracy)
                n = accuracy;
            x = x.substring(0, index) + x.substring( index + 1, index + 1 + n );
            if (n % 2 == 1) {
                n++;
                x += '0';
            }
            x = round(sqrt(x, d), d);
            if (!isThere('.', x))
                x += '.';
            x = fractionPull(x, n / 2);
            return x;
        }
        String r = x, l = "0", num = division("1", sum("1", "1", d), d);
        if (moreAbs(x, "1") == -1)
            r = "1";
        while (moreAbs(sum(r, '-' + l, d), E) > -1) {
            String m = sum(r, l, d);
            m = fastProiz(m, num, d);
            String res = fastProiz(m, m, d);
            if (moreAbs(res, x) < 1)
                l = m;
            else
                r = m;
        }
        accuracy = buf;
        return l;
    }

    public static String toRadians(final String a)
    {
        String ans = "";
        try {ans = division(fastProiz(a, PI, 10), "180", 10);}catch(Exception e){}
        return ans;
    }

    public static String cos(String a, int d)
    {
        a = toOtherNumberSystem(correct(a), d, 10);
        if (a.charAt(0) == '-')
            a = a.substring(1, a.length());
        if (!RAD)
            a = toRadians(a);
        a = applyPeriod(a, fastProiz("2", PI, 10), 10);
        double val = Double.valueOf(a);
        val = Math.cos(val);
        if (abs(val) <= 1e-10)
            val = 0;
        else if (abs(val - 0.5) <= 1e-10)
            val = 0.5;
        else if (abs(val + 0.5) <= 1e-10)
            val = -0.5;
        String ans = correct(String.valueOf(val));
        if (isThere('E', ans)) {
            int p = find('E', ans);
            int n = abs(Integer.valueOf(ans.substring(p + 1, ans.length())));
            ans = ans.substring(0, p);
            ans = fractionPull(ans, n);
        }
        return toOtherNumberSystem(ans, 10, d);
    }

    public static String tg(String a, int d)
    {
        try {
            return division(sin(a, d), cos(a, d), d);
        } catch (Exception e){
            return "error";
        }
    }

    public static String ctg(String a, int d)
    {
        try {
            return division(cos(a, d), sin(a, d), d);
        } catch (Exception e){
            return "error";
        }
    }

    @Override
    public void onClick(final View view)
    {
        str = String.valueOf(input.getText());
        if (view.getId() == R.id.toOtherNumberSystems)
        {
            Intent intent = new Intent(this, ToOtherNumberSystems.class);
            startActivity(intent);
            return;
        }
        if (str.equals("Поле для ввода выражения..."))
            str = "";

        str = calculateFormat(str);
        result = str + "\n\n";
        str = absDetection(str);
        if (!check()){
            Toast.makeText(MainActivity.this, message2, Toast.LENGTH_LONG).show();
            return;
        }
        int val = toNumber(String.valueOf(numberSystem.getText()));
        if (val == -1) {
            result += "Система счисления указана неверно или этот калькулятор не может работать с ней.\n";
            result += "Введите в это поле число от 2 до 36.";
        } else if (str.equals(""))
            result = "Поле ввода пустое! Введите выражение...";
        else try {
                str = value(str, val);
                if (!str.equals("error")) {
                    result += "\nОтвет: " + str + ".\n";
                    result += extraInformation();
                }
                else
                    result = "Обнаружено деление на ноль или возникла другая ошибка!";
            } catch (Exception e) {
                result = "Что-то пошло не так...\n";
                result += "Сбой работы калькулятора мог быть вызван:\n";
                result += "1) Неправильной скобочной последовательностью;\n";
                result += "2) Некорректной расстановкой допустимых символов;\n";
                result += "3) Посторонними символами;\n";
                result += "4) Тем, что на 0 делить нельзя.\n";
                result += "Повторите попытку ввода.\n";
            }
        number = 1;
        Intent intent = new Intent(this, CalcPanel.class);
        startActivity(intent);
    }

    private static String extraInformation()
    {
        String word = "цифр", result = "";
        accuracy = Integer.valueOf(String.valueOf(setAccuracy.getText()));
        if (accuracy > 100) {
            accuracy = 100;
            result = "\nТочность снижена до 100 цифр после запятой...";
        } else if (accuracy < 3){
            accuracy = 3;
            word = "цифры";
            result = "\nТочность повышена до 3 цифр после запятой...";
        } else {
            int a = accuracy % 100;
            if (a % 10 == 1)
                word = "цифра";
            else if (a % 10 > 1 && a % 10 < 5)
                word = "цифры";
            if (a > 9 && a < 21)
                word = "цифр";
        }
        result += "\nВ " + numberSystem.getText() + " системе счисления.";
        result += "\nТочность: " + accuracy + " " + word + " после запятой.";
        return result;
    }

    private static boolean zero(final String s)
    {
        for (int i = 0; i < s.length(); i++) {
            char sim = s.charAt(i);
            if (sim != '.' && sim != '0')
                return false;
        }
        return true;
    }

    private static String calculateFormat(final String s)
    {
        String result = "";
        for (int i = 0; i < s.length(); ++i)
            if (s.charAt(i) == ',')
                result += '.';
            else if (isThere(s.charAt(i), digits + "<>[]{}()+-×/!^|П√stingoc"))
                result += s.charAt(i);
            else if (s.charAt(i) == ':')
                result += '/';
            else if (s.charAt(i) == '*')
                result += '×';
        return result;
    }

    private static String readFormat(final String s)
    {
        String result = "";
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '.')
                result += ',';
            else
                result += s.charAt(i);
        return result;
    }

    private static String absDetection(final String source)
    {
        String s = source;
        if (s.length() > 0 && s.charAt(0) == '|')
            s = '<' + s.substring(1);
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '|') {
                if (!isThere(s.charAt(i - 1), digits + ">)]}!"))
                    s = s.substring(0, i) + '<' + s.substring(i + 1);
                else
                    s = s.substring(0, i) + '>' + s.substring(i + 1);
            }
        }
        return s;
    }

    private boolean check()
    {
        String num = String.valueOf(numberSystem.getText());
        if (num.length() > 2)
            return false;
        for (int i = 0; i < num.length(); ++i)
            if (!isThere(num.charAt(i), "0123456789"))
                return false;
        int val = toNumber(num);
        if (val < 2 || val > 36)
            return false;
        for (int i = 0; i < str.length(); ++i) {
            if (!isThere(str.charAt(i), digits + "<>{}[]()-+:|/*×!^√stcgino"))
                return false;
            else if (isThere(str.charAt(i), digits.substring(0, 36)) && val <= find(str.charAt(i), digits))
                return false;
        }
        if (!corBrackSeq(str))
            return false;
        return true;
    }
}
