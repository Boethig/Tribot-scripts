package scripts.boe_api.banking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class BankItem {
    @NonNull
    public int count;
    public int[] ids;
    public String[] names;
}
