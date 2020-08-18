package classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Intervention {
    private String nomTech,sim,lvcan,psn,matricule,currLoc;
    private User user;
    private static ArrayList<Intervention> listInterventions= new ArrayList<Intervention>();

    public Intervention(JSONObject object){
        try {
            this.psn = object.getString("psn");
            this.sim = object.getString("sim");
            this.lvcan = object.getString("lvcan");
            this.nomTech = object.getString("nomTech");
            this.matricule = object.getString("matricule");
            this.currLoc = object.getString("currLoc");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Intervention(){}
    public Intervention(String nomTech, String sim, String lvcan, String psn, String matricule, String currLoc) {
        this.nomTech = nomTech;
        this.sim = sim;
        this.lvcan = lvcan;
        this.psn = psn;
        this.matricule = matricule;
        this.currLoc = currLoc;
    }

    public String getNomTech() {
        return nomTech;
    }

    public String getSim() {
        return sim;
    }

    public String getLvcan() {
        return lvcan;
    }

    public String getPsn() {
        return psn;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getCurrLoc() {
        return currLoc;
    }

    public static ArrayList<Intervention> getListInterventions() {
        return listInterventions;
    }

    public void setNomTech(String nomTech) {
        this.nomTech = nomTech;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public void setLvcan(String lvcan) {
        this.lvcan = lvcan;
    }

    public void setPsn(String psn) {
        this.psn = psn;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setCurrLoc(String currLoc) {
        this.currLoc = currLoc;
    }

    @Override
    public String toString() {
        return "Intervention{" +
                "nomTech='" + nomTech + '\'' +
                ", sim='" + sim + '\'' +
                ", lvcan='" + lvcan + '\'' +
                ", psn='" + psn + '\'' +
                ", matricule='" + matricule + '\'' +
                ", currLoc='" + currLoc + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
