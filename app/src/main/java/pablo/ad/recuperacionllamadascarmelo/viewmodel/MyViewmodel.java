package pablo.ad.recuperacionllamadascarmelo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import pablo.ad.recuperacionllamadascarmelo.listeners.refreshCalls;
import pablo.ad.recuperacionllamadascarmelo.model.Llamada;

public class MyViewmodel extends AndroidViewModel{

    public static String memoria = "interna";
    private Repository repository;

    public MyViewmodel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }


    public void newCall(String tlf) {
        repository.newCall(tlf);
    }

    public String getCallList(){
        return repository.getTextoCallLogs();
    }

    public void setListener(refreshCalls listener){
        repository.setListener(listener);
    }

}
