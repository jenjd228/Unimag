package com.example.unimag.ui.sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

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

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setSelection(GlobalSort.getInstance().getSpinnerItemPosition());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.categories);

                //GlobalSort.getInstance().setSpinnerItemPosition(selectedItemPosition);
                //GlobalSort.getInstance().setSpinnerSortItemNameCategory(choose[selectedItemPosition]);
                spinnerItemPosition = selectedItemPosition;
                spinnerSortItemNameCategory = choose[selectedItemPosition];

                if (choose[selectedItemPosition].equals("None")){
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


}
