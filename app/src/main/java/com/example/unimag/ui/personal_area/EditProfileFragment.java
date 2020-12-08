package com.example.unimag.ui.personal_area;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;
import com.example.unimag.ui.ThreadCheckingConnection;

public class EditProfileFragment extends Fragment {

    private ImageView editImageUser;
    private EditText editTextFirstName;
    private EditText editTextSurname;
    private EditText editTextSecondSurname;
    private EditText editTextBirthDay;
    private EditText editTextBirthMonth;
    private EditText editTextBirthYear;
    private Button SaveEdit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState, requireContext()); //Проверка на подключение к интернету
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editImageUser = getView().findViewById(R.id.edit_image_user);
        editTextFirstName = getView().findViewById(R.id.edit_name);
        editTextSurname = getView().findViewById(R.id.edit_surname);
        editTextSecondSurname = getView().findViewById(R.id.edit_secondSurname);
        editTextBirthDay = getView().findViewById(R.id.edit_birthDay);
        editTextBirthMonth = getView().findViewById(R.id.edit_birthMonth);
        editTextBirthYear = getView().findViewById(R.id.edit_birthYear);

        /*Сделать setText по умолчанию для всех полей*/
        setImageUser("");

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
    private void setImageUser(String URL) {
        ImageView imageUser = getView().findViewById(R.id.edit_image_user);

        Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user); //Наше изображение
        Bitmap bitmap = scaleCenterCrop(startBitmap, (int) getResources().getDimension(R.dimen.dp150), (int) getResources().getDimension(R.dimen.dp150));
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888); //Наш конечный вариант
        Canvas canvas = new Canvas(output); //Создаем канвас как оутпут

        //Производим закругление
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); //Создаем прямоугольник
        final RectF rectF = new RectF(rect); //Создаем изменяемый прямоуглоьник
        final float roundPx = bitmap.getWidth()/2;; //Закругление в пикселях

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint); //Рисование нашего квадрата с закругленными углами

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        imageUser.setImageBitmap(output);
    }

}
