package com.inuker.bluetooth.library.MysearchDivce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.yukunlin.physiotherapydevice.R;
import com.example.yukunlin.physiotherapydevice.activity.DeviceActivity;
import com.example.yukunlin.physiotherapydevice.module.Device;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.L;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 项目名：PhysiotherapyDevice
 * 包名：com.inuker.bluetooth.library.MysearchDivce
 * 文件名：MySearchDivce
 * 作者 ：梅华黎
 * 联系QQ： ：77299007
 * 创建时间： 2017/10/11 0011 15:13
 * 描述：这里是搜索设备
 */
public class MySearchDivce {

    private static BluetoothClient mClient;
    private static List<Device>  startSearchDevice = new ArrayList<>();


    /**
     * 搜索设备
     */
    public static List<Device>  startSearchDevice( final Activity activity) {
        mClient = new BluetoothClient(activity);
        final ProgressDialog progressDialog = new ProgressDialog( activity);
        progressDialog.setMessage("请稍后...");
        progressDialog.show();
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 1)   // 扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(3000) // 再扫经典蓝牙5s
//                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                startSearchDevice.clear();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Device newDevice = new Device();
                //获取设备名字
                newDevice.setName(device.getName());
                //获取设备 MAC 地址
                newDevice.setMacAddress(device.getAddress());
                newDevice.setId(UUID.randomUUID().toString());
                progressDialog.dismiss();
                mClient.stopSearch();
                startSearchDevice.add(newDevice);
            }

            @Override
            public void onSearchStopped() {
                L.e("onSearchStopped: ");
                if (startSearchDevice.size() == 0) {
                    Toast.makeText(activity, R.string.can_not_find_device, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onSearchCanceled() {
                progressDialog.dismiss();
            }
        });

        return startSearchDevice;
    }
}
