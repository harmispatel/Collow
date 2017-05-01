package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.app.collow.R;
import com.app.collow.activities.FeatureMainActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACCommunityFeaturebean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;

/**
 * Created by harmis on 23/2/17.
 */

public class ACCommunityFeatureAdapter extends RecyclerView.Adapter {



    Activity activity = null;


    public ACCommunityFeatureAdapter(Activity activity) {
        this.activity = activity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_accommunity_features, parent, false);

        vh = new ACCommunityViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ACCommunityFeaturebean communitybean = FeatureMainActivity.accommunityfeaturebeanArrayList.get(position);

        if (CommonMethods.isTextAvailable(communitybean.getTitle())) {
            ((ACCommunityViewHolder) holder).baseTextview_communityfeatures.setText(communitybean.getTitle());
        }


        ((ACCommunityViewHolder) holder).view.setTag(communitybean);
        ((ACCommunityViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent=new Intent(activity, FormsAndDocsDetailActivity.class);
                FormsAndDocsbean formsAndDocsbean1= (FormsAndDocsbean) v.getTag();
                Bundle bundle=new Bundle();
                bundle.putSerializable(BundleCommonKeywords.KEY_FORMANDDOCUMENT_BEAN,formsAndDocsbean1);
                intent.putExtras(bundle);
                activity.startActivity(intent);*/

            }
        });


    }


    @Override
    public int getItemCount() {
        return FeatureMainActivity.accommunityfeaturebeanArrayList.size();
    }



    //
    public static class ACCommunityViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_communityfeatures = null;
        ToggleButton togglebutton_communityfeatures = null;

        View view = null;


        public ACCommunityViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_communityfeatures = (BaseTextview) v.findViewById(R.id.textview_communityfeature_title);
            togglebutton_communityfeatures = (ToggleButton) v.findViewById(R.id.mySwitch_communityfeature);

            view = v;


        }
    }
}

