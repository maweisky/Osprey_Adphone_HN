package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.JshWdjAreaSetActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.AreaGroup;
import osprey_adphone_hn.cellcom.com.cn.bean.AreaGroupManger;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class BaseExpandableListAdapter extends
		android.widget.BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private List<AreaGroup> areagroups;
	private Activity activity;

	public BaseExpandableListAdapter(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
		AreaGroupManger areaGroupManger = new AreaGroupManger();
		areagroups = areaGroupManger.getAreaGroups();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return areagroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return areagroups.get(groupPosition).getAreaChilds().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return areagroups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return areagroups.get(groupPosition).getAreaChilds().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupViewHolder groupViewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_jsh_areagroupitem, null);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.nametv = (TextView) convertView
					.findViewById(R.id.nametv);
			groupViewHolder.left_iv=(ImageView)convertView.findViewById(R.id.left_iv);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		groupViewHolder.nametv.setText(areagroups.get(groupPosition).getName());
		if(isExpanded){			
			groupViewHolder.left_iv.setScaleType(ScaleType.FIT_XY);
			groupViewHolder.left_iv.setImageResource(R.drawable.os_jsh_areah);
		}else{
			groupViewHolder.left_iv.setScaleType(ScaleType.FIT_XY);
			groupViewHolder.left_iv.setImageResource(R.drawable.os_jsh_area);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int groupp = groupPosition;
		final int childp = childPosition;
		ChildViewHolder groupViewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_jsh_areachilditem, null);
			groupViewHolder = new ChildViewHolder();
			groupViewHolder.btn1 = (ImageView) convertView.findViewById(R.id.btn1);
			groupViewHolder.btn2 = (ImageView) convertView.findViewById(R.id.btn2);
			groupViewHolder.btn3 = (ImageView) convertView.findViewById(R.id.btn3);
			groupViewHolder.btn4 = (ImageView) convertView.findViewById(R.id.btn4);
			groupViewHolder.btn5 = (ImageView) convertView.findViewById(R.id.btn5);
			groupViewHolder.btn6 = (ImageView) convertView.findViewById(R.id.btn6);
			groupViewHolder.btn7 = (ImageView) convertView.findViewById(R.id.btn7);
			groupViewHolder.btn8 = (ImageView) convertView.findViewById(R.id.btn8);
			groupViewHolder.btn9 = (ImageView) convertView.findViewById(R.id.btn9);
			groupViewHolder.btn10 = (ImageView) convertView
					.findViewById(R.id.btn10);
			
			groupViewHolder.tv1=(TextView)convertView.findViewById(R.id.tv1);
			groupViewHolder.tv2=(TextView)convertView.findViewById(R.id.tv2);
			groupViewHolder.tv3=(TextView)convertView.findViewById(R.id.tv3);
			groupViewHolder.tv4=(TextView)convertView.findViewById(R.id.tv4);
			groupViewHolder.tv5=(TextView)convertView.findViewById(R.id.tv5);
			groupViewHolder.tv6=(TextView)convertView.findViewById(R.id.tv6);
			groupViewHolder.tv7=(TextView)convertView.findViewById(R.id.tv7);
			groupViewHolder.tv8=(TextView)convertView.findViewById(R.id.tv8);
			groupViewHolder.tv9=(TextView)convertView.findViewById(R.id.tv9);
			groupViewHolder.tv10=(TextView)convertView.findViewById(R.id.tv10);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (ChildViewHolder) convertView.getTag();
		}
		groupViewHolder.tv1.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem1().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem1().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem1().isIsopen()) {
			groupViewHolder.btn1.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn1.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv2.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem2().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem2().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem2().isIsopen()) {
			groupViewHolder.btn2.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn2.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv3.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem3().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem3().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem3().isIsopen()) {
			groupViewHolder.btn3.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn3.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv4.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem4().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem4().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem4().isIsopen()) {
			groupViewHolder.btn4.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn4.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv5.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem5().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem5().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem5().isIsopen()) {
			groupViewHolder.btn5.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn5.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv6.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem6().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem6().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem6().isIsopen()) {
			groupViewHolder.btn6.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn6.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv7.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem7().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem7().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem7().isIsopen()) {
			groupViewHolder.btn7.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn7.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv8.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem8().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem8().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem8().isIsopen()) {
			groupViewHolder.btn8.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn8.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.tv9.setText(areagroups.get(groupPosition)
				.getAreaChilds().get(childPosition).getChildItem9().getName());
		if (areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem9().isIsselect()&&areagroups.get(groupPosition).getAreaChilds().get(childPosition)
				.getChildItem9().isIsopen()) {
			groupViewHolder.btn9.setBackgroundResource(R.drawable.os_wdj_fqh);
		} else {
			groupViewHolder.btn9.setBackgroundResource(R.drawable.os_wdj_fq);
		}
		groupViewHolder.btn10.setBackgroundResource(R.drawable.os_wdj_fq);
		groupViewHolder.btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 0);
			}
		});
		groupViewHolder.btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 1);
			}
		});
		groupViewHolder.btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 2);
			}
		});
		groupViewHolder.btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 3);
			}
		});
		groupViewHolder.btn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 4);
			}
		});
		groupViewHolder.btn6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 5);
			}
		});
		groupViewHolder.btn7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 6);
			}
		});
		groupViewHolder.btn8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 7);
			}
		});
		groupViewHolder.btn9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshWdjAreaSetActivity) activity).childClick(groupp, 8);
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class GroupViewHolder {
		private ImageView left_iv;
		private TextView nametv;
	}

	class ChildViewHolder {
		private ImageView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
				btn10;
		private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
	}
}