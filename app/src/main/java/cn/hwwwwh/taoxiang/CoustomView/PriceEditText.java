package cn.hwwwwh.taoxiang.CoustomView;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by 97481 on 2017/10/7/ 0007.
 */

public class PriceEditText extends EditText {
    /**
     * 小数点前的价格长度
     * 123456789.00 价格长度为 9
     */
    private final int PRICE_LENGTH = 9;
    private CharSequence defaultHint;
    private DecimalFormat decimalFormat;
    private String hint="输入数据过大，请重新输入";

    public PriceEditText(Context context) {
        super(context);
        init();
    }

    public PriceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PriceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * 设置输入类型：数字和小数点
         */
        setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        setMaxLines(1);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(PRICE_LENGTH + 3)});
        defaultHint = getHint();

        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //判断第一位输入是否是“.”
                if (s.toString().startsWith(".")) {
                    s = "0" + s;
                    setText(s);
                    if (s.toString().length() == 2) {
                        setSelection(s.length());
                    }
                    return;
                }
                //判断首位是否是“0”
                if (s.toString().startsWith("0") && s.toString().length() > 1) {
                    //判断第二位不是“.”
                    if (!s.toString().substring(1, 2).equals(".")) {

                        s = s.toString().substring(1, s.toString().length());
                        setText(s);
                        setSelection(s.length());
                        return;
                    }
                }


                //判断小数点后两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        setText(s);
                        setSelection(s.length());
                        return;
                    }
                }

                //判断输入位数
                if (s.toString().length() > PRICE_LENGTH) {
                    if (s.toString().contains(".")) {
                        if (s.toString().indexOf(".") > PRICE_LENGTH) {
                            setText("");
                            setHint(hint);
                            setSelection(0);
                            return;
                        }
                    } else if (s.toString().length() == PRICE_LENGTH + 1) {
                        if (start + 1 == s.toString().length()) {
                            s = s.toString().subSequence(0, PRICE_LENGTH);
                            setText(s);
                            setSelection(s.length());
                            return;
                        } else {
                            setText("");
                            setHint(hint);
                            setSelection(0);
                            return;
                        }
                    } else if (s.toString().length() > PRICE_LENGTH + 1) {
                        setText("");
                        setHint(hint);
                        setSelection(0);
                        return;
                    }
                }

                //还原提示内容
                if (defaultHint != getHint()) {
                    setHint(defaultHint);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 获取double类型的价格
     *
     * @return
     */
    public double getDoublePrice() {
        String s = getText().toString();
        if (TextUtils.isEmpty(s)) return 0.00;
        return Double.parseDouble(s);

    }

    /**
     * 获取string类型的价格
     *
     * @return
     */
    public String getStringPrice() {
        String s = getText().toString();
        if (TextUtils.isEmpty(s)) return "0.00";
        if (decimalFormat == null) decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(Double.parseDouble(s));

    }

    /**
     * 获取string类型的价格(带￥符号)
     *
     * @return
     */
    public String getStringYuan() {
        String s = getText().toString();
        if (TextUtils.isEmpty(s)) return "￥:0.00";
        if (decimalFormat == null) decimalFormat = new DecimalFormat("#0.00");
        return "￥:" + decimalFormat.format(Double.parseDouble(s));

    }

    /**
     * 设置价格
     * @param text
     */
    public void setPrice(String text) {
        double price = 0;
        try {

            price = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        setPrice(price);
    }
    /**
     * 设置价格
     * @param d
     */
    public void setPrice(Double d) {
        if (decimalFormat == null) decimalFormat = new DecimalFormat("#0.00");
        setText(decimalFormat.format(d));
    }
    /**
     * 设置价格
     * @param l
     */
    public void setPrice(long l) {
        if (decimalFormat == null) decimalFormat = new DecimalFormat("#0.00");
        setText(decimalFormat.format(l));
    }
}
