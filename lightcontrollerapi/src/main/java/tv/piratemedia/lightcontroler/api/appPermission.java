package tv.piratemedia.lightcontroler.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/**
 * Created by Eliot Stocker on 14/10/2016.
 */
public class appPermission {
    public String Package;
    public String Sig;

    public appPermission(String Package, String Signature) {
        this.Package = Package;
        this.Sig = Signature;
    }

    public boolean verifyPermission(String pkg, String Sig, Context context) {
        final PackageManager pm = context.getPackageManager();
        Signature[] signs;
        try {
            signs = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES).signatures;
        } catch (final PackageManager.NameNotFoundException e) {
            signs = null;
        }
        if(signs != null) {
            for (Signature signature : signs) {
                if(signature.toCharsString().equals(this.Sig) && signature.toCharsString().equals(Sig)) {
                    return true;
                }
            }
        }
        return false;
    }
}
