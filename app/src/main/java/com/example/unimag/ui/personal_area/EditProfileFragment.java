package com.example.unimag.ui.personal_area;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.MainActivity;
import com.example.unimag.R;
import com.example.unimag.ui.MyCustomPatterns;
import com.example.unimag.ui.Request.SendOrUpdateRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class EditProfileFragment extends Fragment {

    private DataDBHelper dataDbHelper;

    private ImageView editImageUser;

    private String secureKod;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPatronymic;

    private Button saveEdit;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Редактирование профиля");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @SuppressLint("ShowToast")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editImageUser = requireView().findViewById(R.id.edit_image_user);
        editTextName = requireView().findViewById(R.id.edit_name);
        editTextSurname = requireView().findViewById(R.id.edit_surname);
        editTextPatronymic = requireView().findViewById(R.id.edit_secondSurname);
        saveEdit = requireView().findViewById(R.id.register_button2);

        //Кнопка сохранить изменения
        saveEdit.setOnClickListener(view -> {
            String name = String.valueOf(editTextName.getText());
            String surname = String.valueOf(editTextSurname.getText());
            String patronymic = String.valueOf(editTextPatronymic.getText());

            editTextName.setError(null);
            editTextSurname.setError(null);
            editTextPatronymic.setError(null);

            if (!MyCustomPatterns.getInstance().isValidString(surname)){
                editTextSurname.setError("Incorrect surname!");
            }else if (!MyCustomPatterns.getInstance().isValidString(name)){
                editTextName.setError("Incorrect name!");
            }else if (!MyCustomPatterns.getInstance().isValidString(patronymic)){
                editTextPatronymic.setError("Incorrect patronymic!");
            }else {
                String fio = MyCustomPatterns.getInstance().firstCharToUpperCase(surname) + " "
                        + MyCustomPatterns.getInstance().firstCharToUpperCase(name) + " "
                        + MyCustomPatterns.getInstance().firstCharToUpperCase(patronymic);

                SendOrUpdateRequest sendOrUpdateRequest = new SendOrUpdateRequest(getContext(),
                        getParentFragmentManager(),fio,secureKod,"userUpdate");
                sendOrUpdateRequest.execute();

                try {
                    String otvet = sendOrUpdateRequest.get();
                    if (otvet.equals("OK")){
                        Toast.makeText(getContext(),"Успешно!",Toast.LENGTH_LONG).show();
                        editTextPatronymic.setText("");
                        editTextSurname.setText("");
                        editTextName.setText("");
                    }else {
                        Toast.makeText(getContext(),"Ошибка!",Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        });
        /*Сделать setText по умолчанию для всех полей*/
        setImageUser();
    }


    //Функция обрезания фото по центру до нужных размеров c сохранением размеров
    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);
        return dest;
    }


    //Функция установки аватарки пользователя
    private void setImageUser() {
        ImageView imageUser = requireView().findViewById(R.id.edit_image_user);

        Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user); //Наше изображение
        Bitmap bitmap = scaleCenterCrop(startBitmap, (int) getResources().getDimension(R.dimen.dp150), (int) getResources().getDimension(R.dimen.dp150));
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888); //Наш конечный вариант
        Canvas canvas = new Canvas(output); //Создаем канвас как оутпут

        //Производим закругление
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); //Создаем прямоугольник
        final RectF rectF = new RectF(rect); //Создаем изменяемый прямоуглоьник
        final float roundPx = bitmap.getWidth() / 2;
        //Закругление в пикселях

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint); //Рисование нашего квадрата с закругленными углами

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        imageUser.setImageBitmap(output);
    }


    @Override
    public void onDestroyView() {
        ((MainActivity)getActivity()).addInStack(MainActivity.TAB_PERSONAL_AREA, this);
        super.onDestroyView();
    }
}
