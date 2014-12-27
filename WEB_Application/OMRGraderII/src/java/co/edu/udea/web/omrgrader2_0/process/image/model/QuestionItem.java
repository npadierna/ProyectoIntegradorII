package co.edu.udea.web.omrgrader2_0.process.image.model;

import java.util.Arrays;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class QuestionItem {

    private boolean choises[];
    private short id;

    public QuestionItem() {
        this((short) 0, null);
    }

    public QuestionItem(short id, boolean[] choises) {
        super();

        this.setChoises(choises);
        this.setId(id);
    }

    public boolean[] getChoises() {

        return (this.choises);
    }

    public void setChoises(boolean[] choises) {
        this.choises = choises;
    }

    public short getId() {

        return (this.id);
    }

    public void setId(short id) {
        this.id = id;
    }

    @Override()
    public int hashCode() {
        int hash = 5;

        hash = 47 * hash + Arrays.hashCode(this.getChoises());
        hash = 47 * hash + this.getId();

        return (hash);
    }

    @Override()
    public boolean equals(Object obj) {
        if (obj == null) {

            return (false);
        }

        if (getClass() != obj.getClass()) {

            return (false);
        }

        final QuestionItem other = (QuestionItem) obj;
        if (!Arrays.equals(this.getChoises(), other.getChoises())) {

            return (false);
        }

        if (this.getId() != other.getId()) {

            return (false);
        }

        return (true);
    }
}