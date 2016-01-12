package cn.sopho.destiny.gasstation;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;
    ViewPager pager;
    private String titles[];
    private Toolbar toolbar;

    SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titles = new String[]{
                getResources().getString(R.string.tab_head_recent),
                getResources().getString(R.string.tab_head_deal),
                getResources().getString(R.string.tab_head_recommend),
                getResources().getString(R.string.tab_head_pay)};

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
//        String[] values = new String[]{
//                "DEFAULT", "RED", "BLUE", "MATERIAL GREY"
//        };
        String[] values = new String[]{
                getResources().getString(R.string.txt_page_incomplete),
                getResources().getString(R.string.txt_page_incomplete),
                getResources().getString(R.string.txt_page_incomplete),
                getResources().getString(R.string.txt_page_incomplete)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), String.format(
                        getResources().getString(R.string.txt_page_incomplete) ), Toast.LENGTH_SHORT).show();

//                switch (position) {
//                    case 0:
//                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
//                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
//                        mDrawerLayout.closeDrawer(Gravity.START);
//                        break;
//                    case 1:
//                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.red));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.red));
//                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.red));
//                        mDrawerLayout.closeDrawer(Gravity.START);
//
//                        break;
//                    case 2:
//                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.blue));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
//                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.blue));
//                        mDrawerLayout.closeDrawer(Gravity.START);
//
//                        break;
//                    case 3:
//                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
//                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
//                        mDrawerLayout.closeDrawer(Gravity.START);
//
//                        break;
//                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
