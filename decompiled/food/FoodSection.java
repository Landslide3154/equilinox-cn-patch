/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.Trait;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import java.io.IOException;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public interface FoodSection {
    public int eat();

    public boolean canBeEaten();

    public void create(ComponentBundle var1, Trait var2);

    public void getStatusInfo(List<PopUpInfoGui> var1);

    public void load(BinaryReader var1) throws Exception;

    public void export(BinaryWriter var1) throws IOException;
}

