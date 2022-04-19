package swop.Car;

import java.util.List;
import java.util.Map;

public abstract class CarModel {
    private CarModelSpecification carmodelSpecifation = null;
    protected Map<String, List<String>> validOptions;
    protected List<String> mandatoryParts;

    public void setCarModelSpecification(CarModelSpecification selected){
        if (!this.isValidSpecification(selected)){
            throw new IllegalArgumentException("invalid car specification");
        }
        this.carmodelSpecifation = selected;
    };

    public CarModelSpecification getCarModelSpecification() {
        return this.carmodelSpecifation;
    }

    public Map<String, List<String>> getValidOptions(){
        return this.validOptions;
    }

    /**
     * Will check if the generated list of parts contains all the parts.
     * @return true if all parts are valid
     */
    private boolean isValidSpecification(CarModelSpecification specification){
        for(Map.Entry<String,String> selectedMap: specification.getAllParts().entrySet()){
            String option = selectedMap.getKey();
            String selected = selectedMap.getValue();
            if (!validOptions.containsKey(option))
                return false;
            if (!validOptions.get(option).contains(selected))
                return false;
        }
        return this.satisfiesConstraints(specification);
    }

    private boolean satisfiesConstraints(CarModelSpecification specification){
        for(String part : this.mandatoryParts){
            if (!specification.getAllParts().containsKey(part))
                return false;
        }
        if (specification.getPart("Body").equals("sport")){
            if (specification.getPart("Spoiler") == null)
                return false;
            return specification.getPart("Eninge").contains("performance") ||
                    specification.getPart("Eninge").contains("ultra");
        }
        if (specification.getPart("Engine").contains("ultra")){
            return specification.getPart("Airco") == null || specification.getPart("Airco").equals("manual");
        }
        return true;
    }
}
