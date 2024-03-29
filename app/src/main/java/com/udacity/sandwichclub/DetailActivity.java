package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates the UI's individual items corresponding to the given attributes of the sandwich,
     * ( Note: the response of an empty String is collapsing its attribute's view !!! )
     *
     * @param sandwich is the Sandwich given
     */
    private void populateUI(@NonNull Sandwich sandwich) {

        int[] IDs = {R.id.also_known_tv, R.id.origin_tv, R.id.ingredients_tv, R.id.description_tv};
        for (int id: IDs) {
            String string = accessSandwichById(sandwich, id);
            if (!string.equals(""))
                ((TextView) findViewById(id)).setText(string);
            else
                ((LinearLayout) findViewById(id).getParent()) .getLayoutParams().height = 0;
        }
    }

    /**
     * Allows you to access attributes of a given Sandwich via its corresponding UI item's id.
     *
     * @param sandwich is the Sandwich given
     * @param id refers to the UI item dedicated to display the particular Sandwich attribute
     * @return : String, being the value part of the attribute targeted
     */
    @NonNull
    private String accessSandwichById(Sandwich sandwich, int id) {

        switch (id) {
            case R.id.also_known_tv:
                return (sandwich.getAlsoKnownAs() != null) ? interpret(sandwich.getAlsoKnownAs()) : "";
            case R.id.origin_tv:
                return (sandwich.getPlaceOfOrigin() != null) ? sandwich.getPlaceOfOrigin() : "";
            case R.id.ingredients_tv:
                return (sandwich.getIngredients() != null) ? interpret(sandwich.getIngredients()) : "";
            case R.id.description_tv:
                return (sandwich.getDescription() != null) ? sandwich.getDescription() : "";
            default:
                return "";
        }
    }

    /**
     * Interprets a given List of Strings to a single String
     *
     * @param strings is the List of Strings to be converted
     * @return : String, ready to be displayed
     */
    @NonNull
    private String interpret(List<String> strings) {
        String string = strings.toString();
        return string.substring(1, string.length() - 1);
    }
}
