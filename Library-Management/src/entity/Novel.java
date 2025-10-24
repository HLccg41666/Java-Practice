package entity;

import java.util.Date;

public class Novel extends Book {
        private String faction;//派系

        public Novel(String id, String name, String author, double price, String publishTime,String category, String minCategory, String faction) {
            super(id,name,author,price,publishTime,category, minCategory);
            this.faction = faction;
        }
        public Novel(){}

        public String getFaction() {
            return faction;
        }
        public void setFaction(String faction) {
            this.faction = faction;
        }

        @Override
        public String toString(){
            return super.toString() + String.format(" | 派系:%s", faction);
        }

    }
