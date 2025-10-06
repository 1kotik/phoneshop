package util;

import com.es.core.model.Color;
import com.es.core.model.Phone;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class PhoneTestUtils {
    private PhoneTestUtils() {}
    
    public static Phone createPhone() {
        String str = "test";
        int num = 1;
        BigDecimal decimal = new BigDecimal("1.0");
        Phone phone = new Phone();
        
        phone.setBrand(str);
        phone.setModel(str);
        phone.setPrice(decimal);
        phone.setDisplaySizeInches(decimal);
        phone.setWeightGr(num);
        phone.setLengthMm(decimal);
        phone.setWidthMm(decimal);
        phone.setHeightMm(decimal);
        phone.setAnnounced(Date.from(Instant.now()));
        phone.setDeviceType(str);
        phone.setOs(str);
        phone.setDisplayResolution(str);
        phone.setPixelDensity(num);
        phone.setDisplayTechnology(str);
        phone.setBackCameraMegapixels(decimal);
        phone.setFrontCameraMegapixels(decimal);
        phone.setRamGb(decimal);
        phone.setInternalStorageGb(decimal);
        phone.setBatteryCapacityMah(num);
        phone.setTalkTimeHours(decimal);
        phone.setStandByTimeHours(decimal);
        phone.setBluetooth(str);
        phone.setPositioning(str);
        phone.setImageUrl(str);
        phone.setDescription(str);

        return phone;
    }

    public static Color createColor() {
        Color color = new Color();
        color.setId(1L);
        color.setCode("");

        return color;
    }

}
