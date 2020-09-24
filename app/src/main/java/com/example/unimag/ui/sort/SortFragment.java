package com.example.unimag.ui.sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;
import com.example.unimag.ui.ThreadCheckingConnection;

public class SortFragment extends Fragment {

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
        CheckBox checkBox = requireView().findViewById(R.id.sortByTimeCheckBox);


        checkBox.setChecked(GlobalSort.getInstance().getSortByTime());
        checkBox.setOnClickListener(view -> GlobalSort.getInstance().setSortByTime(checkBox.isChecked()));
        //RadioButton buttonDESC = (RadioButton) requireView().findViewById(R.id.radioButtonDESC);

        if (GlobalSort.getInstance().getSortByPricePosition()!=null){
            radioGroup.check(GlobalSort.getInstance().getSortByPricePosition());
        }
        radioGroup.setOnCheckedChangeListener((arg0, selectedId) -> {
            selectedId = radioGroup.getCheckedRadioButtonId();
            GlobalSort.getInstance().setSortByPricePosition(selectedId);
            if (buttonASC.isChecked()){
                GlobalSort.getInstance().setSortByPriceString("sortByPriceASC");
            }else if (buttonDESK.isChecked()){
                GlobalSort.getInstance().setSortByPriceString("sortByPriceDESK");
            }else {
                GlobalSort.getInstance().setSortByPriceString("NO");
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

                GlobalSort.getInstance().setSpinnerItemPosition(selectedItemPosition);
                GlobalSort.getInstance().setSpinnerSortItemNameCategory(choose[selectedItemPosition]);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


}
