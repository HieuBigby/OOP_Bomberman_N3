package uet.oop.bomberman.entities.tile.Item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

public class Item extends Tile {
    public int duration = 100;
    public boolean activated = false;

    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        super.update();
        if(!activated) return;

        if(duration > 0){
            duration--;
        }
    }

    public boolean hasEffect(){
        return activated && duration >= 0;
    }
}
