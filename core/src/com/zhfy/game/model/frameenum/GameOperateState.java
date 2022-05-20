package com.zhfy.game.model.frameenum;

/**
 * 游戏状态
 *
 */
public enum GameOperateState {


    noOperate,//默认



    selectHexagonToTactic,

    selectHexagonToBuild,
    selectHexagonToArmyAct,
    selectHexagonToArmyAtk,
    selectRegionToAirTarget,
    selectRegionToNulTarget,
    selectHexagonToAirborneTactical,
    selectHexagonToAirborne,//空降
    selectHexagonToLegionUnitAirborne,
    selectRegionToLegionAct,
    selectMultipleUnitToLegionAct,
    selectMultipleUnitToLegionTarget,
    selectMultipleUnitToAct;



    public boolean ifNeedRestore(){
        if(   this==GameOperateState.selectHexagonToAirborneTactical||
                this==GameOperateState.selectMultipleUnitToAct||
                this==GameOperateState.selectHexagonToArmyAct||
                this==GameOperateState.selectRegionToAirTarget||
                this==selectHexagonToAirborne||
                this==GameOperateState.selectRegionToNulTarget){
            return true;
        }
        return false;
    }

}
