package rdfcf.helper;
public class DatakeyResponse
{
    private String datakey;

    public String getDatakey ()
    {
        return datakey;
    }

    public void setDatakey (String datakey)
    {


        this.datakey = datakey;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [datakey = "+datakey+"]";
    }
}