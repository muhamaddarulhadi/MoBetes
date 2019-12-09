package com.hadi.mobetes.Model;

public class BSL_data
{
    String dataid;
    String date;
    String time;
    String mood;
    String note;
    String condition;
    float bsl;
    String type;

    public String getDataId()
    {
        return dataid;
    }

    public void setDataId(String dataid)
    {
        this.dataid = dataid;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getCondition()
    {
        return condition;
    }

    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    public float getBsl()
    {
        return bsl;
    }

    public void setBsl(float bsl)
    {
        this.bsl = bsl;
    }
}
