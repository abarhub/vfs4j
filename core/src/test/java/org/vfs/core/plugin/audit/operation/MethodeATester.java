package org.vfs.core.plugin.audit.operation;

import java.util.List;

class MethodeATester {

    private String nomMethode;
    private List<Object> parametres;
    private Object valeurRetour;
    private List<Class<?>> typeParametres;
    private Class<?> typeRetour;
    private String messageLog;
    private List<Object> parametreLog;

    public MethodeATester() {
    }

    public MethodeATester(String nomMethode, List<Object> parametres, Object valeurRetour,
                          List<Class<?>> typeParametres, Class<?> typeRetour,
                          String messageLog, List<Object> parametreLog) {
        this.nomMethode = nomMethode;
        this.parametres = parametres;
        this.valeurRetour = valeurRetour;
        this.typeParametres = typeParametres;
        this.typeRetour = typeRetour;
        this.messageLog = messageLog;
        this.parametreLog = parametreLog;
    }

    public String getNomMethode() {
        return nomMethode;
    }

    public void setNomMethode(String nomMethode) {
        this.nomMethode = nomMethode;
    }

    public List<Object> getParametres() {
        return parametres;
    }

    public void setParametres(List<Object> parametres) {
        this.parametres = parametres;
    }

    public Object getValeurRetour() {
        return valeurRetour;
    }

    public void setValeurRetour(Object valeurRetour) {
        this.valeurRetour = valeurRetour;
    }

    public List<Class<?>> getTypeParametres() {
        return typeParametres;
    }

    public void setTypeParametres(List<Class<?>> typeParametres) {
        this.typeParametres = typeParametres;
    }

    public Class<?> getTypeRetour() {
        return typeRetour;
    }

    public void setTypeRetour(Class<?> typeRetour) {
        this.typeRetour = typeRetour;
    }

    public String getMessageLog() {
        return messageLog;
    }

    public void setMessageLog(String messageLog) {
        this.messageLog = messageLog;
    }

    public List<Object> getParametreLog() {
        return parametreLog;
    }

    public void setParametreLog(List<Object> parametreLog) {
        this.parametreLog = parametreLog;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(nomMethode);
        res.append("(");
        if (typeParametres != null) {
            boolean premier = true;
            for (Class<?> clazz : typeParametres) {
                if (!premier) {
                    res.append(',');
                }
                res.append(clazz.getSimpleName());
                premier = false;
            }
        }
        res.append(")");
        return res.toString();
    }
}
