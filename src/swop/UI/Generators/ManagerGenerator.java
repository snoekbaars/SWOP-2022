package swop.UI.Generators;

import swop.UI.Builders.DisplayStatus;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ManagerGenerator extends UserGenerator {
    public void generateBatchSelection(DisplayStatus builder, List<Map<String, String>> possibleBatch) {
        builder.appendTitle("Possible batchoptions");
        ListIterator<Map<String,String>> it = possibleBatch.listIterator();
        while(it.hasNext()) {
            builder.inputInfo(String.format("%s [%d]", it.next(), (it.nextIndex()-1)));
        }
        builder.endInfo();
        builder.addOption("Select the batch", possibleBatch.size());
    }

    public void generateAlgorithmSelection(DisplayStatus builder,List<String> actions, String activeAlgorithm) {
        builder.initialQuestion("The available algorithms: ");
        builder.appendSubTitle("Current algorithm: " + activeAlgorithm);
        Iterator<String> it = actions.iterator();
        int i = 0;
        while(it.hasNext()) {
            builder.inputInfo(String.format("%s [%s]", it.next(), i));
            i++;
        }
        builder.addOption("Select option", actions.size());
    }
}
