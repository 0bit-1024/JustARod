package org.cneko.justarod.item

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class SlimeRodItem : SelfUsedItem(Settings().maxCount(1).maxDamage(1000)){
    override fun useOnSelf(stack: ItemStack, world: World?, entity: LivingEntity, slot: Int, selected: Boolean): ActionResult {
        // 如果成功使用，就1/500的几率生成一只可爱的小史莱姆
        if(super.useOnSelf(stack, world, entity, slot, selected) == ActionResult.SUCCESS){
            if(world is ServerWorld){
                val serverWorld:ServerWorld = world
                if (Random.create().nextInt(500) == 0) {
                    val nbtCompound = NbtCompound()
                    nbtCompound.putString("id","slime") // id为slime
                    nbtCompound.putInt("Size", 0) // 大小为1
                    nbtCompound.putString("Owner", entity.uuidAsString) // 主人
                    val pos = entity.pos
                    val slime = EntityType.loadEntityWithPassengers(
                        nbtCompound, serverWorld
                    ) { entityx: Entity ->
                        entityx.refreshPositionAndAngles(
                            pos.x,
                            pos.y,
                            pos.z,
                            entityx.yaw,
                            entityx.pitch
                        )
                        entityx
                    }

                    serverWorld.spawnNewEntityAndPassengers(slime)
                    return ActionResult.SUCCESS
                }
            }
        }
        return ActionResult.PASS
    }
}