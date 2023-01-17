package bluper.ftgu.world.item.tier;

public enum HandleTier {
	NOT_APPLICABLE(1, 1),
	OAK(1, 0.9f),
	SPRUCE(0.6f, 0.7f),
	BIRCH(1.1f, 0.8f),
	JUNGLE(1.1f, 0.9f),
	ACACIA(0.9f, 0.8f),
	DARK_OAK(0.9f, 0.8f);

	private final float durability;
	private final float weight;

	HandleTier(float durability, float weight) {
		this.durability = durability;
		this.weight = weight;
	}

	public float getDurability() {
		return durability;
	}

	public float getWeight() {
		return weight;
	}
}
