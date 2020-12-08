package com.example.unimag.ui.sort;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.catalog.CustomGridAdapter;

public class SortFragment extends Fragment {

    private boolean whereFlag = false;

    private Integer sortByPricePosition;

    private Integer spinnerItemPosition = 0;

    private String sortByPriceString = "null";

    private String spinnerSortItemNameCategory = "null"; // категории

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState, requireContext()); //Проверка на подключение к интернету

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spinner = requireView().findViewById(R.id.sortSpinner);
        RadioGroup radioGroup = requireView().findViewById(R.id.radioSortGroup);
        RadioButton buttonASC = requireView().findViewById(R.id.radioButtonASC);
        RadioButton buttonDESK = requireView().findViewById(R.id.radioButtonDESC);
        RadioButton buttonWithoutSort = requireView().findViewById(R.id.radioButtonWithout);
        Button sortButton =  requireView().findViewById(R.id.button_sort);


        if (GlobalSort.getInstance().getSortByPricePosition()!=null){
            radioGroup.check(GlobalSort.getInstance().getSortByPricePosition());
        }

        /**System.out.println("============\n" + GlobalSort.getInstance().getSortByPriceString());
        //Повторная установка прошлых флажков
        if (GlobalSort.getInstance().getSortByPriceString() == "price ASC") {
            buttonASC.setChecked(true);
        } else if (GlobalSort.getInstance().getSortByPriceString() == "price DESC") {
            buttonDESK.setChecked(true);
        } else if (GlobalSort.getInstance().getSortByPriceString() == "NO") {
            buttonWithoutSort.setChecked(true);
        }*/

        //Обновляем флаги
        buttonWithoutSort.setChecked(true);

        radioGroup.setOnCheckedChangeListener((arg0, selectedId) -> {
            selectedId = radioGroup.getCheckedRadioButtonId();
            //GlobalSort.getInstance().setSortByPricePosition(selectedId);
            sortByPricePosition = selectedId;
            if (buttonASC.isChecked()){
                //GlobalSort.getInstance().setSortByPriceString("price ASC");
                sortByPriceString = "price ASC";
            }else if (buttonDESK.isChecked()){
                //GlobalSort.getInstance().setSortByPriceString("price DESC");
                sortByPriceString = "price DESC";
            }else {
                //GlobalSort.getInstance().setSortByPriceString("NO");
                sortByPriceString = "NO";
            }
        });

        //ArrayAdapter<?> adapter =
        //        ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String[] categories = getResources().getStringArray(R.array.categories); //Берем массив с названиями категорий
        AdapterForSpinner adapter = new AdapterForSpinner(requireContext(), R.layout.row_spinner, categories); //Класс указан ниже


        spinner.setAdapter(adapter);

        spinner.setSelection(GlobalSort.getInstance().getSpinnerItemPosition());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.categories);

                //GlobalSort.getInstance().setSpinnerItemPosition(selectedItemPosition);
                //GlobalSort.getInstance().setSpinnerSortItemNameCategory(choose[selectedItemPosition]);
                spinnerItemPosition = selectedItemPosition;

                //Сортируем по категории
                if (choose[selectedItemPosition].equals("Одежда")){
                    spinnerSortItemNameCategory = "Clothes";
                } else if (choose[selectedItemPosition].equals("Сувениры")) {
                    spinnerSortItemNameCategory = "Souvenirs";
                }

                if (choose[selectedItemPosition].equals("Все товары")){
                    //GlobalSort.getInstance().setWhereFlag(false);
                    whereFlag = false;
                }else {
                   // GlobalSort.getInstance().setWhereFlag(true);
                    whereFlag = true;
                    Toast toast = Toast.makeText(getContext(),
                            "Вы выбрали : "+choose[selectedItemPosition]+"", Toast.LENGTH_SHORT);
                    toast.show();
                }
                GlobalSort.getInstance().setUpdateFlag(false);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sortButton.setOnClickListener(view -> {
            GlobalSort.getInstance().setSortByPricePosition(sortByPricePosition);
            GlobalSort.getInstance().setSortByPriceString(sortByPriceString);

            GlobalSort.getInstance().setSpinnerItemPosition(spinnerItemPosition);
            GlobalSort.getInstance().setSpinnerSortItemNameCategory(spinnerSortItemNameCategory);

            GlobalSort.getInstance().setWhereFlag(whereFlag);

            GlobalSort.getInstance().setUpdateFlag(true);

            assert CustomGridAdapter.getInstance() != null;
            CustomGridAdapter.getInstance().cleanList();

            Toast toast = Toast.makeText(getContext(),
                    "Сортировка успешно применена!", Toast.LENGTH_SHORT);
            toast.show();
        });



    }

    /** АДАПТЕР ДЛЯ СПИННЕРА
     *
     *
     *
     * */
    public class AdapterForSpinner extends ArrayAdapter<String> {

        String[] categories; //Массив с названиями категорий

        //Конструктор класса
        public AdapterForSpinner(Context context, int textViewResourceId,
                                 String[] objects) {
            super(context, textViewResourceId, objects);
            categories = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }


        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.text_spinner);
            label.setText(categories[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image_spinner);

            switch (categories[position]) {
                case "Все товары":
                    icon.setImageResource(R.drawable.all_products);
                    break;
                case "Одежда":
                    icon.setImageResource(R.drawable.clothes);
                    break;
                case "Сувениры":
                    icon.setImageResource(R.drawable.souvenirs);
                    break;
                default:
                    break;
            }

            return row;
        }

    }


}
