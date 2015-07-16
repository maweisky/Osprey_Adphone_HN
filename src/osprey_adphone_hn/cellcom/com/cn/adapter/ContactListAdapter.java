package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.ContactBean;
import osprey_adphone_hn.cellcom.com.cn.widget.QuickAlphabeticBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Intents;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<ContactBean> list = new ArrayList<ContactBean>();
	private List<ContactBean> alllist = new ArrayList<ContactBean>();
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private Context ctx;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;

	public ContactListAdapter(Context context, List<ContactBean> list,
			QuickAlphabeticBar alpha) {
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alllist = list;
		finalBitmap = FinalBitmap.create(context);
		loadingBitmap = BitmapFactory.decodeResource(ctx.getResources(),
				R.drawable.os_dhb_itempic);
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			String name = getAlpha(list.get(i).getSortKey());
			if (!alphaIndexer.containsKey(name)) {
				alphaIndexer.put(name, i);
			}
		}

		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
		if (alpha != null) {
			alpha.setAlphaIndexer(alphaIndexer);
		}
	}

	public void setList(List<ContactBean> list) {
		this.list = list;
		this.alllist = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_dhb_contactitem, null);
			holder = new ViewHolder();
			holder.quickContactBadge = (QuickContactBadge) convertView
					.findViewById(R.id.qcb);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.prename = (TextView) convertView.findViewById(R.id.prename);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.prenumber = (TextView) convertView
					.findViewById(R.id.prenumber);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.editiv=(ImageView)convertView.findViewById(R.id.editiv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ContactBean contact = list.get(position);
		holder.prename.setText(contact.getPreDesplayName());
		holder.name.setText(contact.getPosdesplayName());
		holder.prenumber.setText(contact.getPrePhoneNum());
		holder.number.setText(contact.getPosphoneNum());
		// 利用反射机制给QuickContactBadge.mOverlay复制为null
		try {
			Field f = holder.quickContactBadge.getClass().getDeclaredField(
					"mOverlay");
			f.setAccessible(true);
			f.set(holder.quickContactBadge, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!TextUtils.isEmpty(contact.getPhoneNum().trim())){			
			finalBitmap.display(holder.quickContactBadge, "phone:"
					+ contact.getPhoneNum().trim(), loadingBitmap, loadingBitmap,
					true);
		}
		holder.quickContactBadge.assignContactUri(Contacts.getLookupUri(
				contact.getContactId(), contact.getLookUpKey()));
		String currentStr = getAlpha(contact.getSortKey());
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getSortKey()) : " ";

		if (!previewStr.equals(currentStr)) {
//			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
//			holder.alpha.setVisibility(View.GONE);
		}
		holder.editiv.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intentcontact = new Intent(Intent.ACTION_INSERT);
            intentcontact.setType("vnd.android.cursor.dir/person");
            intentcontact.setType("vnd.android.cursor.dir/contact");
            intentcontact.setType("vnd.android.cursor.dir/raw_contact");
            String phone="";
            if(TextUtils.isEmpty(contact.getPrePhoneNum())){
              phone=contact.getPosphoneNum();
            }else{
              phone=contact.getPrePhoneNum()+contact.getPosphoneNum();
            }
            String name="";
            if(TextUtils.isEmpty(contact.getPreDesplayName())){
              name=contact.getPosdesplayName();
            }else{
              name=contact.getPreDesplayName()+contact.getPosdesplayName();
            }
            intentcontact.putExtra(Intents.Insert.PHONE, phone);
            intentcontact.putExtra(Intents.Insert.NAME, name);
            ctx.startActivity(intentcontact);
          }
        });
		return convertView;
	}

	private static class ViewHolder {
		QuickContactBadge quickContactBadge;
		TextView alpha;
		TextView prename;
		TextView name;
		TextView prenumber;
		TextView number;
		ImageView editiv;
	}

	/**
	 * 
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	@Override
	public android.widget.Filter getFilter() {
		android.widget.Filter filter = new android.widget.Filter() {
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				list = (List<ContactBean>) results.values;
				notifyDataSetChanged();
			}

			protected FilterResults performFiltering(CharSequence s) {
				FilterResults results = new FilterResults();
				List<ContactBean> queryResultCities = new ArrayList<ContactBean>();

				if (TextUtils.isEmpty(s)) {
					queryResultCities = alllist;
					results.values = queryResultCities;
					results.count = queryResultCities.size();
					return results;
				} else {
					String str = s.toString();
					if (alllist != null) {
						for (ContactBean contactBean : alllist) {
							// 匹配全拼、首字母、中文城市名、区号、邮政编码
							if ((contactBean.getDesplayName() != null && contactBean
									.getPhoneNum() != null)
									&& (contactBean.getPhoneNum().startsWith(
											str) || contactBean
											.getDesplayName().startsWith(str))) {
								if (contactBean.getPhoneNum().startsWith(str)) {
									String phonenum = contactBean.getPhoneNum();
									if(str.length()<=phonenum.length()){
										phonenum = phonenum.substring(str.length(),
												phonenum.length() );
										contactBean.setPrePhoneNum(str);
										contactBean.setPosphoneNum(phonenum);
									}
								}
								if (contactBean.getDesplayName()
										.startsWith(str)) {
									String desplayName = contactBean
											.getDesplayName();
									if (str.length() <= desplayName.length() ) {
										desplayName = desplayName.substring(
												str.length(),
												desplayName.length());
									} else {
										desplayName = "";
									}
									contactBean.setPreDesplayName(str);
									contactBean.setPosdesplayName(desplayName);
								}
								queryResultCities.add(contactBean);
							}
						}
					}
				}

				results.values = queryResultCities;
				results.count = queryResultCities.size();
				return results;
			}
		};
		return filter;
	}
}
