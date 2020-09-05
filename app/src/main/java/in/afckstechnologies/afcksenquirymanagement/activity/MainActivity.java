package in.afckstechnologies.afcksenquirymanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.ViewPagerAdapter;
import in.afckstechnologies.afcksenquirymanagement.fragments.DisplayStudentEditPreActivity1;
import in.afckstechnologies.afcksenquirymanagement.fragments.FixedSpeedScroller;
import in.afckstechnologies.afcksenquirymanagement.fragments.TabsCoursesActivity;
import in.afckstechnologies.afcksenquirymanagement.fragments.DisplayStudentEditPreActivity;
import in.afckstechnologies.afcksenquirymanagement.fragments.Activity_Location_Details;
import in.afckstechnologies.afcksenquirymanagement.fragments.TemplateDisplayActivity;
import in.afckstechnologies.afcksenquirymanagement.fragments.MultipleCommentAddView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    DisplayStudentEditPreActivity displayStudentEditPreActivity;
    TabsCoursesActivity tabsCoursesActivity;
    Activity_Location_Details activity_location_details;
    TemplateDisplayActivity templateDisplayActivity;
    MultipleCommentAddView multipleCommentAddView;
    //This is our viewPager
    private ViewPager viewPager;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();


        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                prefEditor.putString("templateon", "0");
                                prefEditor.commit();
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_location:
                                prefEditor.putString("templateon", "0");
                                prefEditor.commit();
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_course:
                                prefEditor.putString("templateon", "0");
                                prefEditor.commit();
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.action_template:
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.action_comment:
                                prefEditor.putString("templateon", "0");
                                prefEditor.commit();
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                adapter.notifyDataSetChanged();

                try {
                    Field mScroller;
                    Interpolator sInterpolator = new AccelerateInterpolator();
                    mScroller = ViewPager.class.getDeclaredField("mScroller");
                    mScroller.setAccessible(true);
                    FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), sInterpolator);
                    //scroller.setFixedDuration(5000);
                    mScroller.set(viewPager, scroller);
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe

       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        */
        Intent intent=getIntent();
        int intentFragment=intent.getIntExtra("frgToLoad",-1);
      //  int intentFragment = getIntent().getExtras().getInt("frgToLoad");
        switch (intentFragment){
            case 0:
                // Load corresponding fragment

                viewPager.setCurrentItem(0);
                if (viewPager != null && viewPager.getAdapter() != null) {
                    viewPager.getAdapter().notifyDataSetChanged();
                }
                break;
            case 1:
                // Load corresponding fragment
                break;
            case 2:
                // Load corresponding fragment
                break;
        }
        setupViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        displayStudentEditPreActivity = new DisplayStudentEditPreActivity();
        activity_location_details = new Activity_Location_Details();
        tabsCoursesActivity = new TabsCoursesActivity();
        templateDisplayActivity = new TemplateDisplayActivity();
        multipleCommentAddView = new MultipleCommentAddView();
        adapter.addFragment(displayStudentEditPreActivity);
        adapter.addFragment(activity_location_details);
        adapter.addFragment(tabsCoursesActivity);
        adapter.addFragment(templateDisplayActivity);
        adapter.addFragment(multipleCommentAddView);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);


    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
