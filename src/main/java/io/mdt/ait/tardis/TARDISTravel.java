package io.mdt.ait.tardis;

import com.mdt.ait.core.init.AITBlocks;
import io.mdt.ait.nbt.wrapped.AbsoluteBlockPos;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import io.mdt.ait.util.Schedule;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TARDISTravel extends TARDISLinkableBasic {

    private Schedule schedule;
    private State state;

    public TARDISTravel() {
        this(State.IDLE);
    }

    public TARDISTravel(State state) {
        this.state = state;
    }

    public Result to(AbsoluteBlockPos pos) {
        if (this.state.inProgress()) return Result.IN_PROGRESS;

        TileEntity tile = this.getExterior().getTile();
        CompoundNBT nbt = tile.getTileData();

        this.schedule = new Schedule(
                        () -> {
                            // idle -> demat
                            this.state = this.state.next();
                            // TODO: animation
                        },
                        10)
                .after(new Schedule(
                        () -> {
                            // demat -> vortex

                            //noinspection DataFlowIssue
                            tile.getLevel().destroyBlock(tile.getBlockPos(), false);
                            this.state = this.state.next();
                        },
                        10))
                .after(new Schedule(
                        () -> {
                            // vortex -> remat

                            World world = TARDISUtil.getWorld(pos.getDimension());
                            world.setBlockAndUpdate(
                                    pos, AITBlocks.TARDIS_BLOCK.get().defaultBlockState());

                            //noinspection DataFlowIssue
                            world.getBlockEntity(pos).load(world.getBlockState(pos), nbt);

                            // this.getTARDIS().setPosition(pos);
                            this.getExterior().link(this.getTARDIS());

                            this.state = this.state.next();
                            // TODO: animation
                        },
                        10))
                .after(new Schedule(
                        () -> {
                            // remat -> idle
                            this.state = this.state.next();
                        },
                        10));

        this.schedule.schedule();

        return Result.SUCCESS;
    }
    // TODO:
    // #takeOff()
    // #land()

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public enum State {
        IDLE() {
            @Override
            public boolean canInteract() {
                return true;
            }

            @Override
            public State next() {
                return DEMAT;
            }
        },
        DEMAT() {
            @Override
            public boolean inProgress() {
                return true;
            }

            @Override
            public State next() {
                return VORTEX;
            }
        },
        VORTEX() {
            @Override
            public State next() {
                return REMAT;
            }
        },
        REMAT() {
            @Override
            public boolean inProgress() {
                return true;
            }

            @Override
            public State next() {
                return IDLE;
            }
        };

        public boolean canInteract() {
            return false;
        }

        public boolean inProgress() {
            return false;
        }

        public abstract State next();
    }

    public enum Result {
        SUCCESS,
        IN_PROGRESS
    }
}
