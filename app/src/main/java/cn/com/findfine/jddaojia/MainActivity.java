package cn.com.findfine.jddaojia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import cn.com.findfine.jddaojia.fragment.HomePageFragment;
import cn.com.findfine.jddaojia.fragment.MyInfoFragment;
import cn.com.findfine.jddaojia.fragment.OrderFragment;
import cn.com.findfine.jddaojia.fragment.ShoppingCartFragment;
import cn.com.findfine.jddaojia.fragment.SortFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private HomePageFragment homePageFragment;
    private SortFragment sortFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private OrderFragment orderFragment;
    private MyInfoFragment myInfoFragment;

    private static final String TAG_HOME_PAGE = "HomePage";
    private static final String TAG_SORT = "Sort";
    private static final String TAG_SHOPPING_CART = "ShoppingCart";
    private static final String TAG_ORDER = "Order";
    private static final String TAG_MT_INFO = "MyInfo";

    private String currentPage = TAG_HOME_PAGE;
    private View llHomePage;
    private View llSort;
    private View llShoppintCart;
    private View llOrder;
    private View llMyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setStatusBarColor(Color.TRANSPARENT);

        homePageFragment = HomePageFragment.newInstance();
        sortFragment = SortFragment.newInstance();
        shoppingCartFragment = ShoppingCartFragment.newInstance();
        orderFragment = OrderFragment.newInstance();
        myInfoFragment = MyInfoFragment.newInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_content, homePageFragment, TAG_HOME_PAGE);
        fragmentTransaction.commitAllowingStateLoss();


        llHomePage = findViewById(R.id.ll_home_page);
        llSort = findViewById(R.id.ll_sort);
        llShoppintCart = findViewById(R.id.ll_shoppint_cart);
        llOrder = findViewById(R.id.ll_order);
        llMyInfo = findViewById(R.id.ll_my_info);

        llHomePage.setOnClickListener(this);
        llSort.setOnClickListener(this);
        llShoppintCart.setOnClickListener(this);
        llOrder.setOnClickListener(this);
        llMyInfo.setOnClickListener(this);

        llHomePage.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        selected(v);
        switch (v.getId()) {
            case R.id.ll_home_page:
                showFragment(homePageFragment, TAG_HOME_PAGE);
                break;
            case R.id.ll_sort:
                showFragment(sortFragment, TAG_SORT);
                break;
            case R.id.ll_shoppint_cart:
                showFragment(shoppingCartFragment, TAG_SHOPPING_CART);
                break;
            case R.id.ll_order:
                showFragment(orderFragment, TAG_ORDER);
                break;
            case R.id.ll_my_info:
                showFragment(myInfoFragment, TAG_MT_INFO);
                break;
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        if (tag.equals(currentPage))
            return ;

        currentPage = tag;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment targetFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (targetFragment == null) {
            fragmentTransaction.add(R.id.fl_content, fragment, tag);
        }

        //隐藏其它Fragment
        if (getSupportFragmentManager().getFragments() != null) {
            for (Fragment otherFragment : getSupportFragmentManager().getFragments()) {
                if (!otherFragment.equals(targetFragment)) {
                    fragmentTransaction.hide(otherFragment);
                }
            }
        }

        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void selected(View view) {
        llHomePage.setSelected(false);
        llSort.setSelected(false);
        llShoppintCart.setSelected(false);
        llOrder.setSelected(false);
        llMyInfo.setSelected(false);

        view.setSelected(true);
    }

}
