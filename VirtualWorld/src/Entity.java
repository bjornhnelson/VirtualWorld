public interface Entity extends EntityVisitor {

    <R> R accept(EntityVisitor<R> visitor);

}
