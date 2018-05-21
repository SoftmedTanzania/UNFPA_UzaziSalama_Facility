package apps.uzazisalama.com.anc.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 11/23/17.
 */

public class Locations implements Serializable {

    @SerializedName("locationsHierarchy")
    private LocationHierarchy locationHierarchy;


    public class LocationHierarchy implements Serializable {

        @SerializedName("map")
        private Map map;

        @SerializedName("parentChildren")
        private ParentChildren parentChildren;

    }

    public class Map implements Serializable {

        public Map(){}

    }

    public class ParentChildren implements Serializable {

        public ParentChildren(){}

    }

}
