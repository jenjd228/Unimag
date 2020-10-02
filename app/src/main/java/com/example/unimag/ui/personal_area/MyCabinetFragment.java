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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.UserDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.SneakyThrows;


public class MyCabinetFragment extends Fragment {
    private DataDBHelper dataDbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
    }

    @SneakyThrows
    @Override
    public void onStart() {
        super.onStart();
        String secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
        getDataBySecureKod(secureKod);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        requireView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    return true;
                }
                return false;
            }
        });
    }

    private void getDataBySecureKod(String secureKod) throws ExecutionException, InterruptedException, IOException {
        if (secureKod==null){

        }else {
            GetRequest getRequest = new GetRequest(secureKod,"getUser");
            getRequest.execute();
            if(getRequest.get().equals("Error!")){
                Toast toast = Toast.makeText(getContext(),
                        "Ошибка!", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                UserDTO userDTO = objectMapper.readValue(getRequest.get(), new TypeReference<UserDTO>(){});
                setInformationAboutUser(userDTO.getFio(),userDTO.getEmail(),userDTO.getPoints());
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Личный кабинет");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        View root = inflater.inflate(R.layout.fragment_my_cabinet, container, false);
        return root;
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) { //Функция обрезания фото по центру до нужных размеров c сохранением размеров
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


    private void setImageUser(String URL){ //Функция установки аватарки пользователя
        ImageView imageUser = getView().findViewById(R.id.image_user);

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

    private void setFIO(String fIO){ //Функция установки информации о ФИО пользователя
        TextView varFIO = requireView().findViewById(R.id.text_FIO);
        varFIO.setText(fIO);
    }

    private void setMail(String mail) { //Функция установки информации о mail пользователя
        TextView varMail = requireView().findViewById(R.id.text_mail);
        varMail.setText(mail);
    }

    private void setBalls(float balls) {
        TextView varBalls = requireView().findViewById(R.id.balls);
        varBalls.setText(String.valueOf(balls));
    }

    private void setInformationAboutUser(String fio,String mail,Integer points){ //Функция установки всей информации о пользователе в ЛК
        setFIO(fio);
        setImageUser("Какой-то URL");
        setMail(mail);
        setBalls(points);
    }

    //Установка бокового меню
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_my_cabinet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    //Реагирования пунктов меню на нажатие
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Нажат пункт меню
        switch (item.getItemId()) {
            //Если нажат пункт "Редактировать профиль"
            case R.id.action_settings:
                Navigation.findNavController(requireView()).navigate(R.id.action_myCabinetFragment_to_editProfileFragment);
                return true;
            //Если нажат пункт "Выйти"
            case R.id.action_exit:
                //Прописать
                dataDbHelper.getWritableDatabase().delete(DataDBHelper.TABLE_CONTACTS,null,null);
                Navigation.findNavController(requireView()).navigate(R.id.action_myCabinetFragment_to_navigation_personal_area);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ResetNavigationStack()
    {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        //Установка onClickListener'a для кнопки "Мои заказы"
        TextView buttonMyOrders = requireView().findViewById(R.id.button_orders);
        buttonMyOrders.setOnClickListener(new View.OnClickListener() { //Переход по кнопке "Мои заказы"
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_myCabinetFragment_to_orderFragment);
            }
        });

        //Установка onClickListener'а для кнопки "Обратная связь"
        TextView buttonFeedback = requireView().findViewById(R.id.button_feedback);
        buttonFeedback.setOnClickListener(new View.OnClickListener() { //Переход по кнопке "Мои заказы"
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_myCabinetFragment_to_feedbackFragment);
            }
        });
    }
}
