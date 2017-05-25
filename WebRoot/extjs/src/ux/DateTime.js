/*
 * ��ʱ��ѡ�������ѡ����
 * ת����ע��������gogo1217.iteye.com
*/
Ext.define('Ext.ux.DateTime', {
    extend: 'Ext.picker.Date',//�̳��� Ext.picker.Date
    alias: 'widget.dateptimeicker',//���xtype dateptimeicker
    okText:'ȷ��',//ȷ�ϰ�ť��������
    okTip:'ȷ��',//ȷ�ϰ�ť��ʾ����

    renderTpl: [
        '<div id="{id}-innerEl" role="grid">',
            '<div role="presentation" class="{baseCls}-header">',
                '<a id="{id}-prevEl" class="{baseCls}-prev {baseCls}-arrow" href="#" role="button" title="{prevText}" hidefocus="on" ></a>',
                '<div class="{baseCls}-month" id="{id}-middleBtnEl">{%this.renderMonthBtn(values, out)%}</div>',
                '<a id="{id}-nextEl" class="{baseCls}-next {baseCls}-arrow" href="#" role="button" title="{nextText}" hidefocus="on" ></a>',
            '</div>',
            '<table id="{id}-eventEl" class="{baseCls}-inner" cellspacing="0" role="presentation">',
                '<thead role="presentation"><tr role="presentation">',
                    '<tpl for="dayNames">',
                        '<th role="columnheader" class="{parent.baseCls}-column-header" title="{.}">',
                            '<div class="{parent.baseCls}-column-header-inner">{.:this.firstInitial}</div>',
                        '</th>',
                    '</tpl>',
                '</tr></thead>',
                '<tbody role="presentation"><tr role="presentation">',
                    '<tpl for="days">',
                        '{#:this.isEndOfWeek}',
                        '<td role="gridcell" id="{[Ext.id()]}">',
                           '<a role="presentation" hidefocus="on" class="{parent.baseCls}-date" href="#"></a>',
                        '</td>',
                    '</tpl>',
                '</tr></tbody>',
            '</table>',

            //ָ��ʱ������Ⱦ���
            '<table id="{id}-timeEl" style="table-layout:auto;width:auto;margin:0 3px;" class="x-datepicker-inner" cellspacing="0">',
                '<tbody><tr>',
                    '<td>{%this.renderHourBtn(values,out)%}</td>',
                    '<td>{%this.renderMinuteBtn(values,out)%}</td>',
                    '<td>{%this.renderSecondBtn(values,out)%}</td>',
                '</tr></tbody>',
            '</table>',

            '<tpl if="showToday">',
                //���һ��ȷ�ϰ�ť��Ⱦ
                '<div id="{id}-footerEl" role="presentation" class="{baseCls}-footer">{%this.renderOkBtn(values, out)%}{%this.renderTodayBtn(values, out)%}</div>',
            '</tpl>',
        '</div>',
        {
            firstInitial: function(value) {
                return Ext.picker.Date.prototype.getDayInitial(value);
            },
            isEndOfWeek: function(value) {
                // convert from 1 based index to 0 based
                // by decrementing value once.
                value--;
                var end = value % 7 === 0 && value !== 0;
                return end ? '</tr><tr role="row">' : '';
            },
            renderTodayBtn: function(values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.todayBtn.getRenderTree(), out);
            },
            renderMonthBtn: function(values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.monthBtn.getRenderTree(), out);
            },

            //ָ����Ⱦ��������
            renderHourBtn: function(values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.hourBtn.getRenderTree(), out);//���������������html���
            },
            renderMinuteBtn: function(values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.minuteBtn.getRenderTree(), out);
            },
            renderSecondBtn: function(values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.secondBtn.getRenderTree(), out);
            },
            renderOkBtn: function(values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.okBtn.getRenderTree(), out);
            }
        }
    ],

    beforeRender: function () {
        var me = this,_$Number=Ext.form.field.Number;
        //�������Ⱦ֮ǰ�����Զ�����ӵ�ʱ���֡����ȷ�ϰ�ť���г�ʼ��
        //�����ȿ�����Ҫ�����£�����ʹ�õ�theme��ͬ�������Ҫ����
        me.hourBtn=new _$Number({
            minValue:0,
            maxValue:23,
            step:1,
            width:55
        });
        me.minuteBtn=new _$Number({
            minValue:0,
            maxValue:59,
            step:1,
            width:55,
            labelWidth:10,
            fieldLabel:'&nbsp;'
        });
        me.secondBtn=new _$Number({
            minValue:0,
            maxValue:59,
            step:1,
            width:55,
            labelWidth:10,
            fieldLabel:'&nbsp;'//�����֮ǰ��Ⱦ ':'
        });

        me.okBtn = new Ext.button.Button({
            ownerCt: me,
            ownerLayout: me.getComponentLayout(),
            text: me.okText,
            tooltip: me.okTip,
            tooltipType:'title',
            handler:me.okHandler,//ȷ�ϰ�ť���¼�ί��
            scope: me
        });
        me.callParent();
    },
    
    finishRenderChildren: function () {
        var me = this;
        //�����Ⱦ��ɺ���Ҫ������Ԫ�ص�finishRender���Ӷ�����¼���
        me.hourBtn.finishRender();
        me.minuteBtn.finishRender();
        me.secondBtn.finishRender();
        me.okBtn.finishRender();
        me.callParent();
    },

    /**
     * ȷ�� ��ť�����ĵ���
     */
    okHandler : function(){
        var me = this,
            btn = me.okBtn;

        if(btn && !btn.disabled){
            me.setValue(this.getValue());
            me.fireEvent('select', me, me.value);
            me.onSelect();
        }
        return me;
    },

    /**
     * �����˸���ķ�������Ϊ�������Ǹ���ʱ���getTime�жϵģ������Ҫ��ʱ���֡���ֱ�ֵΪ0���ܱ�֤��ǰֵ������ѡ��
     * @private
     * @param {Date} date The new date
     */
    selectedUpdate: function(date){
        this.callParent([Ext.Date.clearTime(date,true)]);
    },

    /**
     * ����picker����ʾ���ݣ���Ҫͬʱ����ʱ���֡���������ֵ
     * @private
     * @param {Date} date The new date
     * @param {Boolean} forceRefresh True to force a full refresh
     */
    update : function(date, forceRefresh){
        var me = this;
        me.hourBtn.setValue(date.getHours());
        me.minuteBtn.setValue(date.getMinutes());
        me.secondBtn.setValue(date.getSeconds());

        return this.callParent(arguments);
    },

    /**
     * ��pickerѡ�к󣬸�ֵʱ����Ҫ��ʱ���֡���Ҳ��õ�ǰֵ
     * datetimefieldҲ��������������picker��ʼ����������һ��isfixed������
     * @param {Date} date The new date
     * @param {Boolean} isfixed True ʱ�����Դ�ʱ�����л�ȡֵ
    */
    setValue : function(date, isfixed){
        var me = this;
        if(isfixed!==true){
            date.setHours(me.hourBtn.getValue());
            date.setMinutes(me.minuteBtn.getValue());
            date.setSeconds(me.secondBtn.getValue());
        }
        me.value=date;
        me.update(me.value);
        return me;
    },

    // @private
    // @inheritdoc
    beforeDestroy : function() {
        var me = this;

        if (me.rendered) {
            //�������ʱ��Ҳ��Ҫ�����Զ���Ŀؼ�
            Ext.destroy(
                me.hourBtn,
                me.minuteBtn,
                me.secondBtn,
                me.okBtn
            );
        }
        me.callParent();
    }
},
function() {
    var proto = this.prototype,
        date = Ext.Date;

    proto.monthNames = date.monthNames;
    proto.dayNames   = date.dayNames;
    proto.format     = date.defaultFormat;
});
