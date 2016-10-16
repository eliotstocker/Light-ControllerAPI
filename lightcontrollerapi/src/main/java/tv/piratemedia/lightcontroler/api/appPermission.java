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
    public int Sig;

    public appPermission(String Package, int Signature) {
        this.Package = Package;
        this.Sig = Signature;
    }

    public boolean verifyPermission(String pkg, int Sig, Context context) {
        final PackageManager pm = context.getPackageManager();
        Signature[] signs;
        try {
            signs = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES).signatures;
        } catch (final PackageManager.NameNotFoundException e) {
            signs = null;
        }
        if(signs != null) {
            for (Signature signature : signs) {
                if(signature.hashCode() == this.Sig && signature.hashCode() == Sig) {
                    return true;
                }
            }
        }
        return false;
    }
}
