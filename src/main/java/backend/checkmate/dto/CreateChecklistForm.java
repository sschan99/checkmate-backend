package backend.checkmate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateChecklistForm {

    private String title;
    private List<ChecklistItem> content;

    @Getter
    @Setter
    public static class ChecklistItem {
        private String text;
        private boolean checked;
    }
}
