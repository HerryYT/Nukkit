package cn.nukkit.level.generator.standard.pop;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.chunk.IChunk;
import cn.nukkit.level.generator.standard.StandardGeneratorUtils;
import cn.nukkit.registry.BlockRegistry;
import cn.nukkit.utils.ConfigSection;
import lombok.NonNull;
import net.daporkchop.lib.common.util.PValidation;
import net.daporkchop.lib.random.PRandom;

/**
 * Places a given block type using a vanilla bedrock pattern.
 *
 * @author DaPorkchop_
 */
public final class BedrockPopulator implements Populator {
    private final int   startY;
    private final int   runtimeId;
    private final int   step;
    private final int   fade;
    private final int   base;

    public BedrockPopulator(@NonNull ConfigSection config, @NonNull PRandom random) {
        this.runtimeId = BlockRegistry.get().getRuntimeId(StandardGeneratorUtils.parseBlock(config.getString("block", "bedrock")));
        this.startY = config.getInt("startY");
        this.step = config.getBoolean("reverse") ? -1 : 1;
        this.fade = PValidation.ensureNonNegative(config.getInt("fade", -1));
        this.base = PValidation.ensureNonNegative(config.getInt("base", -1));
    }

    @Override
    public void populate(PRandom random, ChunkManager level, int chunkX, int chunkZ) {
        IChunk chunk = level.getChunk(chunkX, chunkZ);

        int y = this.startY;
        for (int i = this.base - 1; i >= 0 && (y & 0xFF) == y; i--, y += this.step) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunk.setBlockRuntimeIdUnsafe(x, y, z, 0, this.runtimeId);
                }
            }
        }
        for (int i = 0; i < this.fade && (y & 0xFF) == y; i++, y += this.step) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (random.nextInt(i + 2) == 0) {
                        chunk.setBlockRuntimeIdUnsafe(x, y, z, 0, this.runtimeId);
                    }
                }
            }
        }
    }
}
