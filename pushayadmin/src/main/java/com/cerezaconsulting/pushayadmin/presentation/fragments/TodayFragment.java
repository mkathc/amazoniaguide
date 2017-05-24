package com.cerezaconsulting.pushayadmin.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerezaconsulting.pushayadmin.R;
import com.cerezaconsulting.pushayadmin.core.BaseActivity;
import com.cerezaconsulting.pushayadmin.core.BaseFragment;
import com.cerezaconsulting.pushayadmin.core.RecyclerViewScrollListener;
import com.cerezaconsulting.pushayadmin.core.ScrollChildSwipeRefreshLayout;
import com.cerezaconsulting.pushayadmin.data.entities.ReservationEntity;
import com.cerezaconsulting.pushayadmin.presentation.adapters.TodayAdapter;
import com.cerezaconsulting.pushayadmin.presentation.contracts.TodayContract;
import com.cerezaconsulting.pushayadmin.presentation.dialogs.ConfirmedDialog;
import com.cerezaconsulting.pushayadmin.presentation.presenters.TodayPresenter;
import com.cerezaconsulting.pushayadmin.presentation.presenters.commons.PlaceItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by katherine on 17/05/17.
 */

public class TodayFragment extends BaseFragment implements TodayContract.View {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.noPlacesIcon)
    ImageView noPlacesIcon;
    @BindView(R.id.noPLacesMain)
    TextView noPLacesMain;
    @BindView(R.id.noPlaces)
    LinearLayout noPlaces;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    private TodayContract.Presenter mPresenter;
    private TodayAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    //private ProgressDialogCustom mProgressDialogCustom;


    public TodayFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
         mPresenter.start();
        mPresenter.loadList();
    }

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TodayPresenter(this,getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.black),
                ContextCompat.getColor(getActivity(), R.color.dark_gray),
                ContextCompat.getColor(getActivity(), R.color.black)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(rvList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mPresenter.start();
                // mPresenter.loadOrdersFromPage(1);
            }
        });

        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(mLayoutManager);

        mAdapter = new TodayAdapter(new ArrayList<ReservationEntity>() , getContext(), (PlaceItem) mPresenter);
        rvList.setAdapter(mAdapter);

    }

    @Override
    public void getTodayList(ArrayList<ReservationEntity> list) {
        mAdapter.setItems(list);

        if (list !=null){
            noPlaces.setVisibility((list.size()>0) ? View.GONE : View.VISIBLE);
        }
        rvList.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }
            @Override
            public void onScrollDown() {

            }
            @Override
            public void onLoadMore() {
                mPresenter.loadfromNextPage();
            }
        });
    }

    @Override
    public void showDetailsTravel(ReservationEntity reservationEntity) {
        Bundle bundle = new Bundle();
        bundle.putInt("num", reservationEntity.getNum_coupons());
        bundle.putSerializable("travel", reservationEntity);
        ConfirmedDialog travelDetailsDialog = new ConfirmedDialog(getContext(), bundle);
        travelDetailsDialog.show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(TodayContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        ((BaseActivity) getActivity()).showMessage(message);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivity) getActivity()).showMessageError(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
